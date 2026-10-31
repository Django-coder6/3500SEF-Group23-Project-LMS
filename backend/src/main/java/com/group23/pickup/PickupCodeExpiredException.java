package com.group23.pickup;

/** Raised when a pickup code has reached its expiry. */
public class PickupCodeExpiredException extends RuntimeException {

    public PickupCodeExpiredException() {
        super("The pickup code has expired. Follow the counter procedure before releasing the parcel.");
    }
}
