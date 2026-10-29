package com.group23.parcel;

import com.group23.security.AuthenticatedUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** Persisted parcel registration and collection-point ownership. */
@Entity
@Table(name = "parcels")
public class Parcel {

    @Id
    private UUID id;

    @Column(name = "tracking_no", nullable = false, unique = true, length = 100)
    private String trackingNo;

    @Column(nullable = false, length = 100)
    private String carrier;

    @Column(name = "recipient_name", nullable = false, length = 255)
    private String recipientName;

    @Column(name = "recipient_phone", nullable = false, length = 50)
    private String recipientPhone;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "parcel_size")
    private ParcelSize size;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "parcel_status")
    private ParcelStatus status;

    @Column(name = "location_id")
    private UUID locationId;

    @Column(name = "registered_by_id", nullable = false)
    private UUID registeredById;

    @Version
    @Column(name = "expected_version", nullable = false)
    private long expectedVersion;

    @Column(name = "collection_point_id", nullable = false)
    private UUID collectionPointId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Parcel() {}

    private Parcel(ParcelCreateRequest request, AuthenticatedUser actor) {
        this.id = UUID.randomUUID();
        this.trackingNo = request.trackingNo().trim();
        this.carrier = request.carrier().trim();
        this.recipientName = request.recipientName().trim();
        this.recipientPhone = request.recipientPhone().trim();
        this.size = request.size();
        this.status = ParcelStatus.registered;
        this.locationId = request.locationId();
        this.registeredById = actor.userId();
        this.collectionPointId = actor.collectionPointId();
    }

    static Parcel register(ParcelCreateRequest request, AuthenticatedUser actor) {
        return new Parcel(request, actor);
    }

    @PrePersist
    void beforeInsert() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void beforeUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public ParcelSize getSize() {
        return size;
    }

    public ParcelStatus getStatus() {
        return status;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public UUID getRegisteredById() {
        return registeredById;
    }

    public long getExpectedVersion() {
        return expectedVersion;
    }

    public UUID getCollectionPointId() {
        return collectionPointId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
