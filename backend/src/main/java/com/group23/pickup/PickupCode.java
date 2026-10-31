package com.group23.pickup;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

/** One parcel's single pickup code and its current lifecycle state. */
@Entity
@Table(name = "pickup_codes")
public class PickupCode {

    @Id
    private UUID id;

    @Column(name = "parcel_id", nullable = false, unique = true)
    private UUID parcelId;

    @JsonIgnore
    @Column(nullable = false, unique = true, length = 32)
    private String code;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "pickup_code_status")
    private PickupCodeStatus status;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used_at")
    private Instant usedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected PickupCode() {}

    public UUID getId() {
        return id;
    }

    public UUID getParcelId() {
        return parcelId;
    }

    @JsonIgnore
    public String getCode() {
        return code;
    }

    public PickupCodeStatus getStatus() {
        return status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getUsedAt() {
        return usedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isExpiredAt(Instant now) {
        return status == PickupCodeStatus.expired || !now.isBefore(expiresAt);
    }

    public void markUsed(Instant now) {
        if (status != PickupCodeStatus.active || isExpiredAt(now)) {
            throw new IllegalStateException("only an active, unexpired pickup code can be used");
        }
        this.status = PickupCodeStatus.used;
        this.usedAt = now;
    }
}
