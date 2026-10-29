package com.group23.parcel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/** Request body for one manual parcel registration. */
public record ParcelCreateRequest(
        @NotBlank(message = "must not be blank") @Size(max = 100, message = "must be at most 100 characters")
                String trackingNo,
        @NotBlank(message = "must not be blank") @Size(max = 100, message = "must be at most 100 characters")
                String carrier,
        @NotBlank(message = "must not be blank") @Size(max = 255, message = "must be at most 255 characters")
                String recipientName,
        @NotBlank(message = "must not be blank") @Size(max = 50, message = "must be at most 50 characters")
                String recipientPhone,
        @NotNull(message = "must be provided") ParcelSize size,
        UUID locationId) {}
