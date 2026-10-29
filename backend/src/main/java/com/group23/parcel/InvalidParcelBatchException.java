package com.group23.parcel;

/** Raised when a batch is empty before individual entries are examined. */
public class InvalidParcelBatchException extends RuntimeException {

    public InvalidParcelBatchException() {
        super("Add at least one parcel to the batch, then submit it again.");
    }
}
