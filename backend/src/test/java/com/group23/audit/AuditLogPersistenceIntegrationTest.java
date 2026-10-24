package com.group23.audit;

import com.group23.security.AuthenticatedUser;
import com.group23.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import({
        AuditLogPersistenceIntegrationTest.AuditTestConfiguration.class,
        AuditLogPersistenceIntegrationTest.AuditTestController.class
})
class AuditLogPersistenceIntegrationTest {

    private static final UUID SESSION_ACTOR_ID =
            UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID FORGED_ACTOR_ID =
            UUID.fromString("20000000-0000-4000-8000-000000000003");
    private static final UUID COLLECTION_POINT_ID =
            UUID.fromString("10000000-0000-4000-8000-000000000001");

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine");

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
        AuthenticatedUser principal = new AuthenticatedUser(
                SESSION_ACTOR_ID,
                UserRole.staff,
                COLLECTION_POINT_ID
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, List.of())
        );
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("TC-PICK-005: confirming a handover writes exactly one masked audit entry")
    void confirmingHandoverWritesExactlyOneEntry() throws Exception {
        ForgedHandover request = new ForgedHandover(
                UUID.randomUUID(),
                FORGED_ACTOR_ID,
                "13812345678"
        );

        mockMvc.perform(post("/test/audit/handover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM audit_logs WHERE action = 'handover.confirm' AND target_id = ?",
                Integer.class,
                request.getTargetId()
        );
        assertThat(count).isEqualTo(1);

        Map<String, Object> row = jdbcTemplate.queryForMap(
                """
                SELECT actor_id, collection_point_id,
                       jsonb_extract_path_text(before, 'request', 'actorId') AS before_actor,
                       jsonb_extract_path_text(before, 'request', 'recipientPhone') AS before_phone,
                       jsonb_extract_path_text(after, 'recipientPhone') AS after_phone
                FROM audit_logs
                WHERE action = 'handover.confirm' AND target_id = ?
                """,
                request.getTargetId()
        );

        assertThat(row.get("actor_id")).isEqualTo(SESSION_ACTOR_ID);
        assertThat(row.get("collection_point_id")).isEqualTo(COLLECTION_POINT_ID);
        assertThat(row.get("before_actor")).isEqualTo("[REDACTED]");
        assertThat(row.get("before_phone")).isEqualTo("138****5678");
        assertThat(row.get("after_phone")).isEqualTo("138****5678");
    }

    @TestConfiguration
    static class AuditTestConfiguration {

        @Bean
        TestAuditService testAuditService() {
            return new TestAuditService();
        }
    }

    static class TestAuditService {

        @Audited(
                action = "handover.confirm",
                targetType = "parcel",
                targetIdExpression = "#result.targetId",
                collectionPointIdExpression = "#result.collectionPointId"
        )
        public HandoverAuditResult confirmHandover(ForgedHandover request) {
            return new HandoverAuditResult(
                    request.targetId,
                    COLLECTION_POINT_ID,
                    request.getRecipientPhone()
            );
        }
    }

    @RestController
    @RequestMapping("/test/audit")
    static class AuditTestController {

        private final TestAuditService auditService;

        AuditTestController(TestAuditService auditService) {
            this.auditService = auditService;
        }

        @PostMapping("/handover")
        HandoverAuditResult confirmHandover(@RequestBody ForgedHandover request) {
            return auditService.confirmHandover(request);
        }
    }

    static class ForgedHandover {

        private final UUID targetId;
        private final UUID actorId;
        private final String recipientPhone;

        ForgedHandover(UUID targetId, UUID actorId, String recipientPhone) {
            this.targetId = targetId;
            this.actorId = actorId;
            this.recipientPhone = recipientPhone;
        }

        public UUID getTargetId() {
            return targetId;
        }

        public UUID getActorId() {
            return actorId;
        }

        public String getRecipientPhone() {
            return recipientPhone;
        }
    }

    static class HandoverAuditResult {

        private final UUID targetId;
        private final UUID collectionPointId;
        private final String recipientPhone;

        HandoverAuditResult(UUID targetId, UUID collectionPointId, String recipientPhone) {
            this.targetId = targetId;
            this.collectionPointId = collectionPointId;
            this.recipientPhone = recipientPhone;
        }

        public UUID getTargetId() {
            return targetId;
        }

        public UUID getCollectionPointId() {
            return collectionPointId;
        }

        public String getRecipientPhone() {
            return recipientPhone;
        }
    }
}
