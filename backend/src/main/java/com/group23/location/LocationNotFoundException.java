package com.group23.location;

/** Raised when a location is absent or belongs to another collection point. */
public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException() {
        super(
                "No storage location with that identifier exists at your collection point. Refresh the board and try again.");
    }
}
