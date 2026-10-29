package com.group23.parcel;

/** Raised when a batch exceeds the approved cap in CR-025. */
public class BatchLimitExceededException extends RuntimeException {

    public BatchLimitExceededException() {
        super("A batch can contain at most 50 parcels. Split the batch manually and submit it again.");
    }
}
