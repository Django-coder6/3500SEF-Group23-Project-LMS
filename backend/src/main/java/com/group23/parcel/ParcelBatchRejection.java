package com.group23.parcel;

/** One rejected entry in a batch registration response. */
public record ParcelBatchRejection(int index, String trackingNo, String reason) {}
