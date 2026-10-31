package com.group23.pickup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** Persisted handover linked to exactly one pickup code and audit entry. */
@Entity
@Table(name = "handovers")
public class Handover {

    @Id
    private UUID id;

    @Column(name = "parcel_id", nullable = false)
    private UUID parcelId;

    @Column(name = "pickup_code_id", nullable = false, unique = true)
    private UUID pickupCodeId;

    @Column(name = "operator_id", nullable = false)
    private UUID operatorId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "handover_result")
    private HandoverResult result;

    @Column(name = "audit_id", nullable = false, unique = true)
    private UUID auditId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Handover() {}

    private Handover(
            UUID parcelId, UUID pickupCodeId, UUID operatorId, HandoverResult result, UUID auditId, Instant createdAt) {
        this.id = UUID.randomUUID();
        this.parcelId = parcelId;
        this.pickupCodeId = pickupCodeId;
        this.operatorId = operatorId;
        this.result = result;
        this.auditId = auditId;
        this.createdAt = createdAt;
    }

    static Handover create(
            UUID parcelId, UUID pickupCodeId, UUID operatorId, HandoverResult result, UUID auditId, Instant createdAt) {
        return new Handover(parcelId, pickupCodeId, operatorId, result, auditId, createdAt);
    }

    public UUID getId() {
        return id;
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

    public UUID getAuditId() {
        return auditId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
