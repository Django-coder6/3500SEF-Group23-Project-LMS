package com.group23.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group23.parcel.ParcelSize;
import com.group23.security.AuthenticatedUser;
import com.group23.user.UserRole;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class LocationServiceTest {

    private static final UUID STAFF_ID = UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID COLLECTION_POINT_ID = UUID.fromString("10000000-0000-4000-8000-000000000001");
    private static final AuthenticatedUser STAFF = new AuthenticatedUser(STAFF_ID, UserRole.staff, COLLECTION_POINT_ID);

    private final LocationRepository locationRepository = mock(LocationRepository.class);
    private final LocationStatusMutationService mutationService = new LocationStatusMutationService(locationRepository);
    private final LocationService service = new LocationService(locationRepository, mutationService);

    @Test
    void listsOnlyTheAuthenticatedStaffCollectionPointInStableOrder() {
        Location available = location(UUID.randomUUID(), LocationStatus.available, null);
        Location disabled = location(UUID.randomUUID(), LocationStatus.disabled, null);
        when(locationRepository.findAllByCollectionPointIdOrderByZoneAscCodeAsc(COLLECTION_POINT_ID))
                .thenReturn(List.of(available, disabled));

        List<LocationResponse> result = service.list(STAFF);

        assertThat(result)
                .extracting(LocationResponse::status)
                .containsExactly(LocationStatus.available, LocationStatus.disabled);
        verify(locationRepository).findAllByCollectionPointIdOrderByZoneAscCodeAsc(COLLECTION_POINT_ID);
    }

    @Test
    void manuallyDisablesAnAvailableLocation() {
        UUID locationId = UUID.randomUUID();
        Location location = location(locationId, LocationStatus.available, null);
        when(locationRepository.findByIdAndCollectionPointId(locationId, COLLECTION_POINT_ID))
                .thenReturn(Optional.of(location));
        when(locationRepository.saveAndFlush(location)).thenReturn(location);

        LocationResponse result =
                service.updateStatus(locationId, new LocationStatusUpdateRequest(LocationStatus.disabled), STAFF);

        assertThat(result.status()).isEqualTo(LocationStatus.disabled);
        assertThat(result.parcelId()).isNull();
        verify(locationRepository).saveAndFlush(location);
    }

    @Test
    void rejectsWorkflowManagedStatesFromManualOverride() {
        UUID locationId = UUID.randomUUID();
        Location reserved = location(locationId, LocationStatus.reserved, UUID.randomUUID());
        when(locationRepository.findByIdAndCollectionPointId(locationId, COLLECTION_POINT_ID))
                .thenReturn(Optional.of(reserved));

        assertThatThrownBy(() -> service.updateStatus(
                        locationId, new LocationStatusUpdateRequest(LocationStatus.available), STAFF))
                .isInstanceOf(InvalidLocationTransitionException.class)
                .hasMessageContaining("parcel operations manage reserved and occupied");
        verify(locationRepository, never()).saveAndFlush(any(Location.class));
    }

    @Test
    void hidesLocationsOutsideTheAuthenticatedCollectionPoint() {
        UUID locationId = UUID.randomUUID();
        when(locationRepository.findByIdAndCollectionPointId(locationId, COLLECTION_POINT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateStatus(
                        locationId, new LocationStatusUpdateRequest(LocationStatus.disabled), STAFF))
                .isInstanceOf(LocationNotFoundException.class);
    }

    @Test
    void rejectsCallersWithoutAStaffCollectionPoint() {
        AuthenticatedUser owner = new AuthenticatedUser(STAFF_ID, UserRole.owner, null);

        assertThatThrownBy(() -> service.list(owner)).isInstanceOf(LocationAccessDeniedException.class);
    }

    private static Location location(UUID id, LocationStatus initialStatus, UUID parcelId) {
        Location location = mock(Location.class);
        AtomicReference<LocationStatus> status = new AtomicReference<>(initialStatus);
        when(location.getId()).thenReturn(id);
        when(location.getCode()).thenReturn("A-01");
        when(location.getZone()).thenReturn("A");
        when(location.getSize()).thenReturn(ParcelSize.small);
        when(location.getStatus()).thenAnswer(invocation -> status.get());
        when(location.getParcelId()).thenReturn(parcelId);
        when(location.getCollectionPointId()).thenReturn(COLLECTION_POINT_ID);
        when(location.getUpdatedAt()).thenReturn(Instant.parse("2026-10-30T00:00:00Z"));
        doAnswer(invocation -> {
                    status.set(invocation.getArgument(0));
                    return null;
                })
                .when(location)
                .changeStatus(any(LocationStatus.class));
        return location;
    }
}
