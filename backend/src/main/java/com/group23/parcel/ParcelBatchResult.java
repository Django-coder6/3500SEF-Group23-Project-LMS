package com.group23.parcel;

import java.util.List;

/** Per-entry result for a batch parcel registration. */
public record ParcelBatchResult(List<ParcelResponse> created, List<ParcelBatchRejection> rejected) {}
