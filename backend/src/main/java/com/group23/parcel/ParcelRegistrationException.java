package com.group23.parcel;

/** A registration failure that is safe to report to the API caller. */
public class ParcelRegistrationException extends RuntimeException {

    public ParcelRegistrationException(String message) {
        super(message);
    }

    public ParcelRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
