package com.group23.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group23.security.AuthenticatedUser;
import com.group23.user.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.SimpleTransactionStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuditLogAspectTest {

    private static final UUID SESSION_ACTOR_ID =
            UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID FORGED_ACTOR_ID =
            UUID.fromString("20000000-0000-4000-8000-000000000003");
    private static final UUID COLLECTION_POINT_ID =
            UUID.fromString("10000000-0000-4000-8000-000000000001");

    private final AuditLogRepository repository = mock(AuditLogRepository.class);
    private final PlatformTransactionManager transactionManager =
            mock(PlatformTransactionManager.class);

    @BeforeEach
    void setUp() {
        when(transactionManager.getTransaction(any(TransactionDefinition.class)))
                .thenReturn(new SimpleTransactionStatus());
        when(repository.append(any())).thenReturn(UUID.randomUUID());

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
    void oneMutationWritesOneRowUsingTheSessionActorAndMaskedSnapshots() {
        AuditPayloadSanitizer sanitizer =
                new AuditPayloadSanitizer(new ObjectMapper().findAndRegisterModules());
        AuditLogAspect aspect =
                new AuditLogAspect(repository, sanitizer, transactionManager);
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestMutationService());
        factory.addAspect(aspect);
        TestMutationService service = factory.getProxy();

        ForgedMutation request = new ForgedMutation(
                FORGED_ACTOR_ID,
                "13812345678"
        );
        MutationResult result = service.confirmHandover(request);

        assertThat(result.getTargetId()).isNotNull();
        assertThat(result.getCollectionPointId()).isEqualTo(COLLECTION_POINT_ID);
        verify(repository).append(org.mockito.ArgumentMatchers.argThat(entry ->
                entry.actorId().equals(SESSION_ACTOR_ID)
                        && entry.targetId().equals(result.getTargetId())
                        && entry.collectionPointId().equals(COLLECTION_POINT_ID)
                        && entry.action().equals("handover.confirm")
                        && entry.targetType().equals("parcel")
                        && entry.before().toString().contains("138****5678")
                        && entry.before().toString().contains("[REDACTED]")
                        && !entry.before().toString().contains("13812345678")
                        && !entry.before().toString().contains(FORGED_ACTOR_ID.toString())
                        && entry.after().toString().contains("138****5678")
        ));
    }

    static class TestMutationService {

        @Audited(
                action = "handover.confirm",
                targetType = "parcel",
                targetIdExpression = "#result.targetId",
                collectionPointIdExpression = "#result.collectionPointId"
        )
        public MutationResult confirmHandover(ForgedMutation request) {
            return new MutationResult(
                    UUID.randomUUID(),
                    COLLECTION_POINT_ID,
                    request.getRecipientPhone()
            );
        }
    }

    static class ForgedMutation {

        private final UUID actorId;
        private final String recipientPhone;

        ForgedMutation(UUID actorId, String recipientPhone) {
            this.actorId = actorId;
            this.recipientPhone = recipientPhone;
        }

        public UUID getActorId() {
            return actorId;
        }

        public String getRecipientPhone() {
            return recipientPhone;
        }
    }

    static class MutationResult {

        private final UUID targetId;
        private final UUID collectionPointId;
        private final String recipientPhone;

        MutationResult(UUID targetId, UUID collectionPointId, String recipientPhone) {
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
