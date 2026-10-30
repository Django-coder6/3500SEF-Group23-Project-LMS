package com.group23.location;

import com.group23.audit.Audited;
import com.group23.security.AuthenticatedUser;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Applies one audited manual state change to an authorized location. */
@Service
public class LocationStatusMutationService {

    private static final Map<LocationStatus, Set<LocationStatus>> MANUAL_TRANSITIONS = Map.of(
            LocationStatus.available, Set.of(LocationStatus.disabled),
            LocationStatus.disabled, Set.of(LocationStatus.available));

    private final LocationRepository locationRepository;

    public LocationStatusMutationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Audited(
            action = "location.status.update",
            targetType = "location",
            targetIdExpression = "#result.id",
            collectionPointIdExpression = "#a2.collectionPointId")
    @Transactional
    public LocationResponse update(Location location, LocationStatusUpdateRequest request, AuthenticatedUser actor) {
        if (!location.getCollectionPointId().equals(actor.collectionPointId())) {
            throw new LocationNotFoundException();
        }
        if (!MANUAL_TRANSITIONS.getOrDefault(location.getStatus(), Set.of()).contains(request.status())) {
            throw new InvalidLocationTransitionException(location.getStatus(), request.status());
        }

        location.changeStatus(request.status());
        return LocationResponse.from(locationRepository.saveAndFlush(location));
    }
}
