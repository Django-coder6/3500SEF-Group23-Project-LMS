package com.group23.pickup;

import com.group23.location.Location;
import com.group23.location.LocationRepository;
import com.group23.location.LocationStatus;
import com.group23.parcel.Parcel;
import com.group23.parcel.ParcelRepository;
import com.group23.parcel.ParcelStatus;
import com.group23.security.AuthenticatedUser;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Confirms one pickup atomically and makes repeat confirmation idempotent. */
@Service
public class PickupConfirmationService {

    private final PickupCodeRepository pickupCodeRepository;
    private final HandoverRepository handoverRepository;
    private final ParcelRepository parcelRepository;
    private final LocationRepository locationRepository;
    private final HandoverMutationService handoverMutationService;

    public PickupConfirmationService(
            PickupCodeRepository pickupCodeRepository,
            HandoverRepository handoverRepository,
            ParcelRepository parcelRepository,
            LocationRepository locationRepository,
            HandoverMutationService handoverMutationService) {
        this.pickupCodeRepository = pickupCodeRepository;
        this.handoverRepository = handoverRepository;
        this.parcelRepository = parcelRepository;
        this.locationRepository = locationRepository;
        this.handoverMutationService = handoverMutationService;
    }

    @Transactional
    public HandoverResponse confirm(PickupConfirmationRequest request, AuthenticatedUser actor) {
        requireCollectedResult(request.result());

        PickupCode pickupCode = pickupCodeRepository
                .findForUpdateByCode(request.pickupCode())
                .orElseThrow(PickupCodeInvalidException::new);
        Parcel parcel = parcelRepository.findById(pickupCode.getParcelId()).orElseThrow(ParcelNotOnShelfException::new);
        requireCollectionPoint(parcel, actor);

        if (pickupCode.getStatus() == PickupCodeStatus.revoked) {
            throw new PickupCodeInvalidException();
        }
        if (pickupCode.getStatus() == PickupCodeStatus.used) {
            Optional<Handover> existing = handoverRepository.findByPickupCodeId(pickupCode.getId());
            if (existing.isPresent()) {
                return HandoverResponse.from(existing.get());
            }
            throw new PickupCodeUsedException();
        }
        if (pickupCode.isExpiredAt(Instant.now())) {
            throw new PickupCodeExpiredException();
        }

        Location location = requireOccupiedShelf(parcel, actor);
        return handoverMutationService
                .complete(pickupCode, parcel, location, actor)
                .response();
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

    private static void requireCollectedResult(HandoverResult result) {
        if (result != null && result != HandoverResult.collected) {
            throw new InvalidHandoverResultException();
        }
    }
}
