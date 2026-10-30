package com.group23.location;

import com.group23.security.AuthenticatedUser;
import com.group23.user.UserRole;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Lists and manually maintains storage locations within one collection point. */
@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationStatusMutationService mutationService;

    public LocationService(LocationRepository locationRepository, LocationStatusMutationService mutationService) {
        this.locationRepository = locationRepository;
        this.mutationService = mutationService;
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> list(AuthenticatedUser actor) {
        UUID collectionPointId = requireStaffCollectionPoint(actor);
        return locationRepository.findAllByCollectionPointIdOrderByZoneAscCodeAsc(collectionPointId).stream()
                .map(LocationResponse::from)
                .toList();
    }

    @Transactional
    public LocationResponse updateStatus(
            UUID locationId, LocationStatusUpdateRequest request, AuthenticatedUser actor) {
        UUID collectionPointId = requireStaffCollectionPoint(actor);
        Location location = locationRepository
                .findByIdAndCollectionPointId(locationId, collectionPointId)
                .orElseThrow(LocationNotFoundException::new);
        return mutationService.update(location, request, actor);
    }

    private static UUID requireStaffCollectionPoint(AuthenticatedUser actor) {
        if (actor == null || actor.role() != UserRole.staff || actor.collectionPointId() == null) {
            throw new LocationAccessDeniedException();
        }
        return actor.collectionPointId();
    }
}
