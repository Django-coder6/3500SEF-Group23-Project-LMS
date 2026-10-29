package com.group23.parcel;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/** Request body for manual batch parcel registration. */
public record ParcelBatchCreateRequest(
        @NotEmpty(message = "must contain at least one parcel") List<ParcelCreateRequest> parcels) {}
