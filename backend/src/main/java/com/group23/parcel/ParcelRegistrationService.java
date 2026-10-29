package com.group23.parcel;

import com.group23.audit.Audited;
import com.group23.security.AuthenticatedUser;
import java.util.Locale;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Registers one parcel and writes its audit entry in the same transaction. */
@Service
public class ParcelRegistrationService {

    private static final String TRACKING_NO_CONSTRAINT = "uq_parcels_tracking_no";

    private final ParcelRepository parcelRepository;
    private final ParcelLocationPolicy locationPolicy;

    public ParcelRegistrationService(ParcelRepository parcelRepository, ParcelLocationPolicy locationPolicy) {
        this.parcelRepository = parcelRepository;
        this.locationPolicy = locationPolicy;
    }

    @Audited(
            action = "parcel.register",
            targetType = "parcel",
            targetIdExpression = "#result.id",
            collectionPointIdExpression = "#a1.collectionPointId")
    @Transactional
    public ParcelResponse register(ParcelCreateRequest request, AuthenticatedUser actor) {
        requireCollectionPoint(actor);
        requireOwnedLocation(request.locationId(), actor.collectionPointId());
        try {
            Parcel parcel = parcelRepository.saveAndFlush(Parcel.register(request, actor));
            return ParcelResponse.from(parcel);
        } catch (DataIntegrityViolationException exception) {
            if (isDuplicateTrackingNo(exception)) {
                throw new DuplicateTrackingNoException();
            }
            throw new ParcelRegistrationException(
                    "Check that the storage location belongs to your collection point, then submit the parcel again.",
                    exception);
        }
    }

    static void requireCollectionPoint(AuthenticatedUser actor) {
        if (actor == null || actor.collectionPointId() == null) {
            throw new MissingCollectionPointException();
        }
    }

    private void requireOwnedLocation(UUID locationId, UUID collectionPointId) {
        if (locationId != null && !locationPolicy.belongsToCollectionPoint(locationId, collectionPointId)) {
            throw new ParcelRegistrationException(
                    "Choose a storage location from your collection point, then submit the parcel again.");
        }
    }

    private static boolean isDuplicateTrackingNo(DataIntegrityViolationException exception) {
        Throwable cause = exception.getMostSpecificCause();
        String message = cause == null ? exception.getMessage() : cause.getMessage();
        return message != null && message.toLowerCase(Locale.ROOT).contains(TRACKING_NO_CONSTRAINT);
    }
}
