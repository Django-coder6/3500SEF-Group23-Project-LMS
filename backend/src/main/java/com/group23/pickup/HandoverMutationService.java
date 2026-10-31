package com.group23.pickup;

import com.group23.audit.AuditAttachable;
import com.group23.audit.Audited;
import com.group23.location.Location;
import com.group23.parcel.Parcel;
import com.group23.security.AuthenticatedUser;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Applies one audited handover mutation after the outer service has validated it. */
@Service
public class HandoverMutationService {

    private final HandoverRepository handoverRepository;

    public HandoverMutationService(HandoverRepository handoverRepository) {
        this.handoverRepository = handoverRepository;
    }

    @Audited(
            action = "handover.confirm",
            targetType = "parcel",
            targetIdExpression = "#a1.id",
            collectionPointIdExpression = "#a3.collectionPointId")
    @Transactional
    public HandoverMutationOutcome complete(
            PickupCode pickupCode, Parcel parcel, Location location, AuthenticatedUser actor) {
        Instant now = Instant.now();
        pickupCode.markUsed(now);
        parcel.collect();
        location.release();
        return new PendingHandover(
                handoverRepository,
                parcel.getId(),
                pickupCode.getId(),
                actor.userId(),
                HandoverResult.collected,
                actor.collectionPointId());
    }

    static final class PendingHandover implements AuditAttachable, HandoverMutationOutcome {

        private final HandoverRepository handoverRepository;
        private final UUID parcelId;
        private final UUID pickupCodeId;
        private final UUID operatorId;
        private final HandoverResult result;
        private final UUID collectionPointId;

        PendingHandover(
                HandoverRepository handoverRepository,
                UUID parcelId,
                UUID pickupCodeId,
                UUID operatorId,
                HandoverResult result,
                UUID collectionPointId) {
            this.handoverRepository = handoverRepository;
            this.parcelId = parcelId;
            this.pickupCodeId = pickupCodeId;
            this.operatorId = operatorId;
            this.result = result;
            this.collectionPointId = collectionPointId;
        }

        @Override
        public Object attachAudit(UUID auditId) {
            Instant createdAt = Instant.now();
            Handover handover = handoverRepository.saveAndFlush(
                    Handover.create(parcelId, pickupCodeId, operatorId, result, auditId, createdAt));
            return new CompletedHandover(HandoverResponse.from(handover));
        }

        @Override
        public HandoverResponse response() {
            throw new IllegalStateException("handover response is unavailable before audit attachment");
        }

        public UUID getParcelId() {
            return parcelId;
        }

        public UUID getPickupCodeId() {
            return pickupCodeId;
        }

        public UUID getOperatorId() {
            return operatorId;
        }

        public HandoverResult getResult() {
            return result;
        }

        public UUID getCollectionPointId() {
            return collectionPointId;
        }
    }

    private record CompletedHandover(HandoverResponse handoverResponse) implements HandoverMutationOutcome {

        @Override
        public HandoverResponse response() {
            return handoverResponse;
        }
    }
}
