package com.group23.pickup;

/** Raised when a used code has no handover that can be returned idempotently. */
public class PickupCodeUsedException extends RuntimeException {

    public PickupCodeUsedException() {
        super("The pickup code has already been used. Do not hand over the parcel again.");
    }
}
