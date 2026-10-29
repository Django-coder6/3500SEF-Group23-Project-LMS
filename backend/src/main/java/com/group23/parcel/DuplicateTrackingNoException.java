package com.group23.parcel;

/** Raised when the database rejects an already registered waybill. */
public class DuplicateTrackingNoException extends ParcelRegistrationException {

    public DuplicateTrackingNoException() {
        super("This tracking number is already registered. Open the existing parcel or use a different number.");
    }
}
