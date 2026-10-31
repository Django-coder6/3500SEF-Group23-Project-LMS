package com.group23.pickup;

/** Raised when a code is unknown, revoked, or belongs to another collection point. */
public class PickupCodeInvalidException extends RuntimeException {

    public PickupCodeInvalidException() {
        super("The pickup code is invalid. Check the code and try again.");
    }
}
