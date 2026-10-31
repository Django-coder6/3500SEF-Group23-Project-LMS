package com.group23.pickup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Request body for confirming one parcel handover. */
public record PickupConfirmationRequest(
        @NotBlank(message = "must not be blank") @Size(max = 32, message = "must be at most 32 characters")
                String pickupCode,
        HandoverResult result) {}
