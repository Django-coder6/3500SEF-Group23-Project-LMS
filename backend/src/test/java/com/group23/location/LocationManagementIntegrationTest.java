package com.group23.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group23.security.AuthenticatedUser;
import com.group23.user.UserRole;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class LocationManagementIntegrationTest {

    private static final UUID STAFF_ID = UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID COLLECTION_POINT_ID = UUID.fromString("10000000-0000-4000-8000-000000000001");
    private static final UUID AVAILABLE_LOCATION_ID = UUID.fromString("30000000-0000-4000-8000-000000000003");
    private static final UUID RESERVED_LOCATION_ID = UUID.fromString("30000000-0000-4000-8000-000000000001");
    private static final UUID OTHER_POINT_LOCATION_ID = UUID.fromString("30000000-0000-4000-8000-000000000005");

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void authenticateStaff() {
        AuthenticatedUser principal = new AuthenticatedUser(STAFF_ID, UserRole.staff, COLLECTION_POINT_ID);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, List.of()));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listsOnlyTheStaffCollectionPointWithAllFourStates() throws Exception {
        mockMvc.perform(get("/api/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[*].status")
                        .value(containsInAnyOrder("available", "reserved", "occupied", "disabled")))
                .andExpect(jsonPath("$[*].id", not(hasItem(OTHER_POINT_LOCATION_ID.toString()))));
    }

    @Test
    void manuallyDisablesAndReenablesALocationWithAuditTrail() throws Exception {
        updateStatus(AVAILABLE_LOCATION_ID, LocationStatus.disabled)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("disabled"))
                .andExpect(jsonPath("$.parcelId").value(org.hamcrest.Matchers.nullValue()));

        String storedStatus = jdbcTemplate.queryForObject(
                "SELECT status FROM locations WHERE id = ?", String.class, AVAILABLE_LOCATION_ID);
        assertThat(storedStatus).isEqualTo("disabled");

        Map<String, Object> audit = jdbcTemplate.queryForMap(
                """
                SELECT actor_id, collection_point_id,
                       before #>> '{location,status}' AS before_status,
                       before #>> '{request,status}' AS requested_status,
                       after ->> 'status' AS after_status
                FROM audit_logs
                WHERE action = 'location.status.update' AND target_id = ?
                """,
                AVAILABLE_LOCATION_ID);
        assertThat(audit.get("actor_id")).isEqualTo(STAFF_ID);
        assertThat(audit.get("collection_point_id")).isEqualTo(COLLECTION_POINT_ID);
        assertThat(audit.get("before_status")).isEqualTo("available");
        assertThat(audit.get("requested_status")).isEqualTo("disabled");
        assertThat(audit.get("after_status")).isEqualTo("disabled");

        updateStatus(AVAILABLE_LOCATION_ID, LocationStatus.available).andExpect(status().isOk());
    }

    @Test
    void cannotChangeALocationAtAnotherCollectionPoint() throws Exception {
        updateStatus(OTHER_POINT_LOCATION_ID, LocationStatus.disabled)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("NOT_FOUND"));
    }

    @Test
    void cannotManuallyMoveAWorkflowManagedLocation() throws Exception {
        updateStatus(RESERVED_LOCATION_ID, LocationStatus.available)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error.code").value("UNPROCESSABLE_ENTITY"))
                .andExpect(jsonPath("$.error.message")
                        .value(org.hamcrest.Matchers.containsString("parcel operations manage")));
    }

    @Test
    void recommendationEndpointRemainsUnimplemented() throws Exception {
        mockMvc.perform(
                        post("/api/locations/recommend")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"size":"small"}
                                """))
                .andExpect(status().isNotFound());
    }

    private org.springframework.test.web.servlet.ResultActions updateStatus(
            UUID locationId, LocationStatus targetStatus) throws Exception {
        return mockMvc.perform(patch("/api/locations/{id}/status", locationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LocationStatusUpdateRequest(targetStatus))));
    }
}
