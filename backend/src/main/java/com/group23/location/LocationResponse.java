package com.group23.location;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.group23.parcel.ParcelSize;
import java.util.UUID;

/** API representation of one storage location. */
@JsonPropertyOrder({"id", "code", "zone", "size", "status", "parcelId"})
public record LocationResponse(
        UUID id, String code, String zone, ParcelSize size, LocationStatus status, UUID parcelId) {

    public static LocationResponse from(Location location) {
        return new LocationResponse(
                location.getId(),
                location.getCode(),
                location.getZone(),
                location.getSize(),
                location.getStatus(),
                location.getParcelId());
    }
}
