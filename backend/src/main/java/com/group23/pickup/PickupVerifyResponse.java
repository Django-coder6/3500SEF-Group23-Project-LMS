package com.group23.pickup;

import com.group23.location.LocationResponse;
import com.group23.parcel.ParcelResponse;

/** Parcel and location summary returned after a valid pickup-code check. */
public record PickupVerifyResponse(String pickupCode, ParcelResponse parcelSummary, LocationResponse location) {}
