package com.group23.pickup;

import java.time.Instant;
import java.util.UUID;

/** Public representation of a completed handover. */
public record HandoverResponse(UUID id, UUID parcelId, HandoverResult result, Instant createdAt) {

    public static HandoverResponse from(Handover handover) {
        return new HandoverResponse(
                handover.getId(), handover.getParcelId(), handover.getResult(), handover.getCreatedAt());
    }
}
