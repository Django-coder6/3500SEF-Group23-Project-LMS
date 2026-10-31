package com.group23.pickup;

import com.group23.location.Location;
import com.group23.location.LocationRepository;
import com.group23.location.LocationResponse;
import com.group23.location.LocationStatus;
import com.group23.parcel.Parcel;
import com.group23.parcel.ParcelRepository;
import com.group23.parcel.ParcelResponse;
import com.group23.parcel.ParcelStatus;
import com.group23.security.AuthenticatedUser;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Validates a pickup code and returns the parcel and shelf shown to staff. */
@Service
public class PickupVerificationService {

    private final PickupCodeRepository pickupCodeRepository;
    private final ParcelRepository parcelRepository;
    private final LocationRepository locationRepository;

    public PickupVerificationService(
            PickupCodeRepository pickupCodeRepository,
            ParcelRepository parcelRepository,
            LocationRepository locationRepository) {
        this.pickupCodeRepository = pickupCodeRepository;
        this.parcelRepository = parcelRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional(readOnly = true)
    public PickupVerifyResponse verify(PickupVerifyRequest request, AuthenticatedUser actor) {
        PickupCode pickupCode =
                pickupCodeRepository.findByCode(request.pickupCode()).orElseThrow(PickupCodeInvalidException::new);
        Parcel parcel = parcelRepository.findById(pickupCode.getParcelId()).orElseThrow(ParcelNotOnShelfException::new);
        requireCollectionPoint(parcel, actor);

        if (pickupCode.getStatus() == PickupCodeStatus.revoked) {
            throw new PickupCodeInvalidException();
        }
        if (pickupCode.getStatus() == PickupCodeStatus.used) {
            throw new PickupCodeUsedException();
        }
        if (pickupCode.isExpiredAt(Instant.now())) {
            throw new PickupCodeExpiredException();
        }

        Location location = requireOccupiedShelf(parcel, actor);
        return new PickupVerifyResponse(
                request.pickupCode(), ParcelResponse.from(parcel), LocationResponse.from(location));
    }

    private Location requireOccupiedShelf(Parcel parcel, AuthenticatedUser actor) {
        if (parcel.getStatus() != ParcelStatus.shelved || parcel.getLocationId() == null) {
            throw new ParcelNotOnShelfException();
        }
        Location location = locationRepository
                .findByIdAndCollectionPointId(parcel.getLocationId(), actor.collectionPointId())
                .orElseThrow(ParcelNotOnShelfException::new);
        if (location.getStatus() != LocationStatus.occupied || !parcel.getId().equals(location.getParcelId())) {
            throw new ParcelNotOnShelfException();
        }
        return location;
    }

    private static void requireCollectionPoint(Parcel parcel, AuthenticatedUser actor) {
        if (!parcel.getCollectionPointId().equals(actor.collectionPointId())) {
            throw new PickupCodeInvalidException();
        }
    }
}
