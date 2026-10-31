package com.group23.pickup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Request body for pickup-code verification. */
public record PickupVerifyRequest(
        @NotBlank(message = "must not be blank") @Size(max = 32, message = "must be at most 32 characters")
                String pickupCode) {}
