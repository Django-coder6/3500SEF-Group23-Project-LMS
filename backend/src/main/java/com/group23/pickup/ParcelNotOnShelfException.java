package com.group23.pickup;

/** Raised when parcel and location records do not describe one occupied shelf link. */
public class ParcelNotOnShelfException extends RuntimeException {

    public ParcelNotOnShelfException() {
        super("The parcel is not currently on an occupied shelf at this collection point.");
    }
}
