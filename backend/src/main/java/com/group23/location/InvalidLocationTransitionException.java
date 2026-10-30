package com.group23.location;

/** Raised when a workflow-managed location state is changed manually. */
public class InvalidLocationTransitionException extends RuntimeException {

    public InvalidLocationTransitionException(LocationStatus current, LocationStatus requested) {
        super("A location cannot be changed manually from " + current + " to " + requested
                + ". Use available or disabled for a manual change; parcel operations manage reserved and occupied.");
    }
}
