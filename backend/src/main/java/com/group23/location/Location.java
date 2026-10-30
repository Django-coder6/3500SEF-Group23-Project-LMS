package com.group23.location;

import com.group23.parcel.ParcelSize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** Persisted storage location and its optional parcel occupation. */
@Entity
@Table(name = "locations")
public class Location {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, length = 100)
    private String zone;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "parcel_size")
    private ParcelSize size;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "location_status")
    private LocationStatus status;

    @Column(name = "parcel_id")
    private UUID parcelId;

    @Column(name = "collection_point_id", nullable = false)
    private UUID collectionPointId;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Location() {}

    void changeStatus(LocationStatus newStatus) {
        this.status = newStatus;
    }

    @PreUpdate
    void beforeUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getZone() {
        return zone;
    }

    public ParcelSize getSize() {
        return size;
    }

    public LocationStatus getStatus() {
        return status;
    }

    public UUID getParcelId() {
        return parcelId;
    }

    public UUID getCollectionPointId() {
        return collectionPointId;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
