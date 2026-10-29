package com.group23.parcel;

/** Raised when a staff account is not assigned to a collection point. */
public class MissingCollectionPointException extends RuntimeException {

    public MissingCollectionPointException() {
        super("Ask an administrator to assign this staff account to a collection point, then sign in again.");
    }
}
