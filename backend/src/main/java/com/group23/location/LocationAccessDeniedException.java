package com.group23.location;

/** Raised when the caller cannot act within one assigned collection point. */
public class LocationAccessDeniedException extends RuntimeException {

    public LocationAccessDeniedException() {
        super("Sign in with a staff account assigned to a collection point to manage storage locations.");
    }
}
