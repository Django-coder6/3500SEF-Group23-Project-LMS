package com.group23.pickup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group23.security.AuthenticatedUser;
import com.group23.user.UserRole;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
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
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PickupHandoverIntegrationTest {

    private static final UUID STAFF_A_ID = UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID POINT_A_ID = UUID.fromString("10000000-0000-4000-8000-000000000001");
    private static final UUID STAFF_B_ID = UUID.fromString("20000000-0000-4000-8000-000000000003");
    private static final UUID POINT_B_ID = UUID.fromString("10000000-0000-4000-8000-000000000002");

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
    void authenticateStaffA() {
        authenticate(STAFF_A_ID, POINT_A_ID);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void verifiesAnActiveCodeAndReturnsItsParcelAndShelf() throws Exception {
        ShelfFixture fixture =
                createShelf(POINT_A_ID, STAFF_A_ID, "active", Instant.now().plusSeconds(3_600));

        mockMvc.perform(verifyRequest(fixture.pickupCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pickupCode").value(fixture.pickupCode()))
                .andExpect(
                        jsonPath("$.parcelSummary.id").value(fixture.parcelId().toString()))
                .andExpect(jsonPath("$.parcelSummary.status").value("shelved"))
                .andExpect(jsonPath("$.parcelSummary.recipientPhone").value("138****5678"))
                .andExpect(jsonPath("$.parcelSummary.locationId")
                        .value(fixture.locationId().toString()))
                .andExpect(jsonPath("$.location.id").value(fixture.locationId().toString()))
                .andExpect(jsonPath("$.location.status").value("occupied"))
                .andExpect(
                        jsonPath("$.location.parcelId").value(fixture.parcelId().toString()));
    }

    @Test
    void returnsContractErrorCodesForUnknownExpiredAndUsedCodes() throws Exception {
        ShelfFixture expired =
                createShelf(POINT_A_ID, STAFF_A_ID, "active", Instant.now().minusSeconds(60));
        ShelfFixture used =
                createShelf(POINT_A_ID, STAFF_A_ID, "used", Instant.now().plusSeconds(3_600));

        mockMvc.perform(verifyRequest("UNKNOWN-" + UUID.randomUUID().toString().substring(0, 8)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("PICKUP_CODE_INVALID"));
        mockMvc.perform(verifyRequest(expired.pickupCode()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("PICKUP_CODE_EXPIRED"));
        mockMvc.perform(confirmRequest(used.pickupCode()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("PICKUP_CODE_USED"));
    }

    @Test
    void hidesCodesAndParcelsOwnedByAnotherCollectionPoint() throws Exception {
        ShelfFixture otherPoint =
                createShelf(POINT_B_ID, STAFF_B_ID, "active", Instant.now().plusSeconds(3_600));

        mockMvc.perform(verifyRequest(otherPoint.pickupCode()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("PICKUP_CODE_INVALID"));
        mockMvc.perform(confirmRequest(otherPoint.pickupCode()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("PICKUP_CODE_INVALID"));
    }

    @Test
    void rejectsAParcelWhoseLocationIsNoLongerOccupiedByIt() throws Exception {
        ShelfFixture fixture =
                createShelf(POINT_A_ID, STAFF_A_ID, "active", Instant.now().plusSeconds(3_600));
        jdbcTemplate.update(
                "UPDATE locations SET status = 'reserved'::location_status WHERE id = ?", fixture.locationId());

        mockMvc.perform(verifyRequest(fixture.pickupCode()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("PARCEL_NOT_ON_SHELF"));
        mockMvc.perform(confirmRequest(fixture.pickupCode()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("PARCEL_NOT_ON_SHELF"));
    }

    @Test
    void confirmationUpdatesTheShelfSafelyAndWritesOneMaskedAuditEntry() throws Exception {
        ShelfFixture fixture =
                createShelf(POINT_A_ID, STAFF_A_ID, "active", Instant.now().plusSeconds(3_600));

        mockMvc.perform(confirmRequest(fixture.pickupCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parcelId").value(fixture.parcelId().toString()))
                .andExpect(jsonPath("$.result").value("collected"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isNotEmpty());

        assertOperationalState(fixture);

        Map<String, Object> handover = jdbcTemplate.queryForMap(
                """
                SELECT operator_id, result::text AS result
                FROM handovers
                WHERE parcel_id = ?
                """,
                fixture.parcelId());
        assertThat(handover.get("operator_id")).isEqualTo(STAFF_A_ID);
        assertThat(handover.get("result")).isEqualTo("collected");

        Map<String, Object> audit = jdbcTemplate.queryForMap(
                """
                SELECT actor_id, collection_point_id, before::text AS before_payload, after::text AS after_payload
                FROM audit_logs
                WHERE action = 'handover.confirm' AND target_id = ?
                """,
                fixture.parcelId());
        assertThat(audit.get("actor_id")).isEqualTo(STAFF_A_ID);
        assertThat(audit.get("collection_point_id")).isEqualTo(POINT_A_ID);
        assertThat(audit.get("before_payload").toString())
                .contains("[REDACTED]")
                .contains("138****5678")
                .doesNotContain(fixture.pickupCode());
        assertThat(audit.get("after_payload").toString()).doesNotContain(fixture.pickupCode());
    }

    @Test
    void repeatConfirmationReturnsTheSameHandoverWithoutWritingMoreRows() throws Exception {
        ShelfFixture fixture =
                createShelf(POINT_A_ID, STAFF_A_ID, "active", Instant.now().plusSeconds(3_600));

        String first = mockMvc.perform(confirmRequest(fixture.pickupCode()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String second = mockMvc.perform(confirmRequest(fixture.pickupCode()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(second).isEqualTo(first);
        assertThat(count("handovers", "parcel_id", fixture.parcelId())).isEqualTo(1);
        assertThat(count("audit_logs", "target_id", fixture.parcelId())).isEqualTo(1);
    }

    @Test
    @Timeout(20)
    void concurrentConfirmationCreatesExactlyOneHandoverAndAuditEntry() throws Exception {
        ShelfFixture fixture =
                createShelf(POINT_A_ID, STAFF_A_ID, "active", Instant.now().plusSeconds(3_600));
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try {
            List<Future<MvcResult>> results = List.of(
                    executor.submit(() -> confirmAsStaffA(fixture.pickupCode(), ready, start)),
                    executor.submit(() -> confirmAsStaffA(fixture.pickupCode(), ready, start)));
            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            assertThat(results.get(0).get(10, TimeUnit.SECONDS).getResponse().getStatus())
                    .isEqualTo(200);
            assertThat(results.get(1).get(10, TimeUnit.SECONDS).getResponse().getStatus())
                    .isEqualTo(200);
        } finally {
            start.countDown();
            executor.shutdownNow();
        }

        assertOperationalState(fixture);
        assertThat(count("handovers", "parcel_id", fixture.parcelId())).isEqualTo(1);
        assertThat(count("audit_logs", "target_id", fixture.parcelId())).isEqualTo(1);
    }

    private MvcResult confirmAsStaffA(String pickupCode, CountDownLatch ready, CountDownLatch start) throws Exception {
        authenticate(STAFF_A_ID, POINT_A_ID);
        try {
            ready.countDown();
            if (!start.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("concurrent confirmation did not start");
            }
            return mockMvc.perform(confirmRequest(pickupCode)).andReturn();
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private ShelfFixture createShelf(
            UUID collectionPointId, UUID registeredById, String codeStatus, Instant expiresAt) {
        UUID parcelId = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        UUID pickupCodeId = UUID.randomUUID();
        String pickupCode = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        Instant usedAt = "used".equals(codeStatus) ? Instant.now() : null;

        jdbcTemplate.update(
                """
                INSERT INTO parcels (
                    id, tracking_no, carrier, recipient_name, recipient_phone, size, status,
                    location_id, registered_by_id, pickup_code_id, expected_version, collection_point_id
                )
                VALUES (?, ?, 'SF Express', 'Recipient', '13812345678', 'small'::parcel_size,
                        'shelved'::parcel_status, NULL, ?, NULL, 1, ?)
                """,
                parcelId,
                "T-" + parcelId,
                registeredById,
                collectionPointId);
        jdbcTemplate.update(
                """
                INSERT INTO locations (
                    id, code, zone, size, status, parcel_id, collection_point_id
                )
                VALUES (?, ?, 'Z', 'small'::parcel_size, 'occupied'::location_status, ?, ?)
                """,
                locationId,
                "L-" + locationId,
                parcelId,
                collectionPointId);
        jdbcTemplate.update(
                """
                INSERT INTO pickup_codes (id, parcel_id, code, status, expires_at, used_at)
                VALUES (?, ?, ?, CAST(? AS pickup_code_status), ?, ?)
                """,
                pickupCodeId,
                parcelId,
                pickupCode,
                codeStatus,
                expiresAt,
                usedAt);
        jdbcTemplate.update(
                "UPDATE parcels SET location_id = ?, pickup_code_id = ? WHERE id = ?",
                locationId,
                pickupCodeId,
                parcelId);
        return new ShelfFixture(parcelId, locationId, pickupCodeId, pickupCode);
    }

    private void assertOperationalState(ShelfFixture fixture) {
        Map<String, Object> parcel = jdbcTemplate.queryForMap(
                "SELECT status::text AS status, location_id FROM parcels WHERE id = ?", fixture.parcelId());
        Map<String, Object> location = jdbcTemplate.queryForMap(
                "SELECT status::text AS status, parcel_id FROM locations WHERE id = ?", fixture.locationId());
        Map<String, Object> pickupCode = jdbcTemplate.queryForMap(
                "SELECT status::text AS status, used_at FROM pickup_codes WHERE id = ?", fixture.pickupCodeId());

        assertThat(parcel.get("status")).isEqualTo("collected");
        assertThat(parcel.get("location_id")).isNull();
        assertThat(location.get("status")).isEqualTo("available");
        assertThat(location.get("parcel_id")).isNull();
        assertThat(pickupCode.get("status")).isEqualTo("used");
        assertThat(pickupCode.get("used_at")).isNotNull();
    }

    private int count(String table, String column, UUID value) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM " + table + " WHERE " + column + " = ?", Integer.class, value);
        return count == null ? 0 : count;
    }

    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder verifyRequest(
            String pickupCode) {
        return post("/api/pickup/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(new PickupVerifyRequest(pickupCode)));
    }

    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder confirmRequest(
            String pickupCode) {
        return post("/api/pickup/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(new PickupConfirmationRequest(pickupCode, HandoverResult.collected)));
    }

    private String json(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (com.fasterxml.jackson.core.JsonProcessingException exception) {
            throw new IllegalStateException("test request could not be serialized", exception);
        }
    }

    private static void authenticate(UUID staffId, UUID collectionPointId) {
        AuthenticatedUser principal = new AuthenticatedUser(staffId, UserRole.staff, collectionPointId);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, List.of()));
    }

    private record ShelfFixture(UUID parcelId, UUID locationId, UUID pickupCodeId, String pickupCode) {}
}
