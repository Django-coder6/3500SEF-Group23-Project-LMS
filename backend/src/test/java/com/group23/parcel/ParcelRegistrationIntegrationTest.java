package com.group23.parcel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group23.security.AuthenticatedUser;
import com.group23.user.UserRole;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
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
class ParcelRegistrationIntegrationTest {

    private static final UUID STAFF_ID = UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID COLLECTION_POINT_ID = UUID.fromString("10000000-0000-4000-8000-000000000001");

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
    void createsParcelAndWritesOneMaskedAuditEntry() throws Exception {
        ParcelCreateRequest request = request("INT-" + UUID.randomUUID());

        String response = mockMvc.perform(post("/api/parcels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.trackingNo").value(request.trackingNo()))
                .andExpect(jsonPath("$.recipientPhone").value("138****5678"))
                .andExpect(jsonPath("$.status").value("registered"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UUID parcelId =
                UUID.fromString(objectMapper.readTree(response).get("id").asText());
        Map<String, Object> parcel = jdbcTemplate.queryForMap(
                "SELECT registered_by_id, collection_point_id, status FROM parcels WHERE id = ?", parcelId);
        assertThat(parcel.get("registered_by_id")).isEqualTo(STAFF_ID);
        assertThat(parcel.get("collection_point_id")).isEqualTo(COLLECTION_POINT_ID);
        assertThat(parcel.get("status")).isEqualTo("registered");

        Map<String, Object> audit = jdbcTemplate.queryForMap(
                """
                SELECT actor_id, collection_point_id,
                       jsonb_extract_path_text(before, 'request', 'recipientPhone') AS before_phone,
                       jsonb_extract_path_text(before, 'request', 'recipientName') AS before_name,
                       jsonb_extract_path_text(after, 'recipientPhone') AS after_phone,
                       jsonb_extract_path_text(after, 'recipientName') AS after_name
                FROM audit_logs
                WHERE action = 'parcel.register' AND target_id = ?
                """,
                parcelId);
        assertThat(audit.get("actor_id")).isEqualTo(STAFF_ID);
        assertThat(audit.get("collection_point_id")).isEqualTo(COLLECTION_POINT_ID);
        assertThat(audit.get("before_phone")).isEqualTo("138****5678");
        assertThat(audit.get("before_name")).isEqualTo("[REDACTED]");
        assertThat(audit.get("after_phone")).isEqualTo("138****5678");
        assertThat(audit.get("after_name")).isEqualTo("[REDACTED]");
    }

    @Test
    void databaseUniqueConstraintRejectsADuplicateTrackingNo() throws Exception {
        ParcelCreateRequest request = request("DUP-" + UUID.randomUUID());

        mockMvc.perform(post("/api/parcels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/parcels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("DUPLICATE_TRACKING_NO"));

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM parcels WHERE tracking_no = ?", Integer.class, request.trackingNo());
        assertThat(count).isEqualTo(1);
    }

    @Test
    void batchOverFiftyIsRejectedWithoutCreatingAnyParcels() throws Exception {
        String prefix = "LIMIT-" + UUID.randomUUID() + "-";
        List<ParcelCreateRequest> parcels = IntStream.range(0, 51)
                .mapToObj(index -> request(prefix + index))
                .toList();

        mockMvc.perform(post("/api/parcels/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ParcelBatchCreateRequest(parcels))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BATCH_LIMIT_EXCEEDED"))
                .andExpect(jsonPath("$.error.message").value(org.hamcrest.Matchers.containsString("manually")));

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM parcels WHERE tracking_no LIKE ?", Integer.class, prefix + "%");
        assertThat(count).isZero();
    }

    @Test
    void batchCreatesValidEntryAndReportsDuplicateEntry() throws Exception {
        ParcelCreateRequest duplicate = request("BATCH-DUP-" + UUID.randomUUID());
        mockMvc.perform(post("/api/parcels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isCreated());

        ParcelCreateRequest valid = request("BATCH-VALID-" + UUID.randomUUID());
        ParcelBatchCreateRequest batch = new ParcelBatchCreateRequest(List.of(valid, duplicate));
        mockMvc.perform(post("/api/parcels/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batch)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.created[0].trackingNo").value(valid.trackingNo()))
                .andExpect(jsonPath("$.rejected[0].index").value(1))
                .andExpect(jsonPath("$.rejected[0].trackingNo").value(duplicate.trackingNo()));
    }

    @Test
    void rejectsALocationOwnedByAnotherCollectionPoint() throws Exception {
        UUID locationAtAnotherPoint = UUID.fromString("30000000-0000-4000-8000-000000000005");
        ParcelCreateRequest request = new ParcelCreateRequest(
                "CROSS-POINT-" + UUID.randomUUID(),
                "SF Express",
                "Recipient",
                "13812345678",
                ParcelSize.small,
                locationAtAnotherPoint);

        mockMvc.perform(post("/api/parcels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error.message")
                        .value(org.hamcrest.Matchers.containsString("from your collection point")));
    }

    private static ParcelCreateRequest request(String trackingNo) {
        return new ParcelCreateRequest(trackingNo, "SF Express", "Recipient", "13812345678", ParcelSize.small, null);
    }
}
