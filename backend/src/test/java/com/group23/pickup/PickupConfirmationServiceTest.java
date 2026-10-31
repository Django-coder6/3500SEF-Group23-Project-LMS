package com.group23.pickup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group23.location.Location;
import com.group23.location.LocationRepository;
import com.group23.location.LocationStatus;
import com.group23.parcel.Parcel;
import com.group23.parcel.ParcelRepository;
import com.group23.parcel.ParcelStatus;
import com.group23.security.AuthenticatedUser;
import com.group23.user.UserRole;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PickupConfirmationServiceTest {

    private static final UUID STAFF_ID = UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID COLLECTION_POINT_ID = UUID.fromString("10000000-0000-4000-8000-000000000001");
    private static final AuthenticatedUser STAFF = new AuthenticatedUser(STAFF_ID, UserRole.staff, COLLECTION_POINT_ID);

    private final PickupCodeRepository pickupCodeRepository = mock(PickupCodeRepository.class);
    private final HandoverRepository handoverRepository = mock(HandoverRepository.class);
    private final ParcelRepository parcelRepository = mock(ParcelRepository.class);
    private final LocationRepository locationRepository = mock(LocationRepository.class);
    private final HandoverMutationService handoverMutationService = mock(HandoverMutationService.class);
    private final PickupConfirmationService service = new PickupConfirmationService(
            pickupCodeRepository, handoverRepository, parcelRepository, locationRepository, handoverMutationService);

    @Test
    void completesAndReturnsTheHandoverForAnActiveCode() {
        PickupCode pickupCode = activeCode();
        Parcel parcel = shelvedParcel();
        Location location = occupiedLocation(parcel.getLocationId(), parcel.getId());
        HandoverResponse expected =
                new HandoverResponse(UUID.randomUUID(), parcel.getId(), HandoverResult.collected, Instant.now());
        givenActiveShelf(pickupCode, parcel, location);
        when(handoverMutationService.complete(pickupCode, parcel, location, STAFF))
                .thenReturn(() -> expected);

        HandoverResponse response =
                service.confirm(new PickupConfirmationRequest("PICK-1", HandoverResult.collected), STAFF);

        assertThat(response).isEqualTo(expected);
        verify(handoverMutationService).complete(pickupCode, parcel, location, STAFF);
    }

    @Test
    void returnsTheRecordedHandoverWhenTheCodeWasAlreadyUsed() {
        PickupCode pickupCode = activeCode();
        Parcel parcel = shelvedParcel();
        Handover handover = mock(Handover.class);
        HandoverResponse expected =
                new HandoverResponse(UUID.randomUUID(), parcel.getId(), HandoverResult.collected, Instant.now());
        UUID parcelId = parcel.getId();
        UUID pickupCodeId = pickupCode.getId();
        when(pickupCode.getStatus()).thenReturn(PickupCodeStatus.used);
        when(pickupCodeRepository.findForUpdateByCode("PICK-1")).thenReturn(Optional.of(pickupCode));
        when(pickupCode.getParcelId()).thenReturn(parcelId);
        when(parcelRepository.findById(parcelId)).thenReturn(Optional.of(parcel));
        when(handoverRepository.findByPickupCodeId(pickupCodeId)).thenReturn(Optional.of(handover));
        stubHandoverResponse(handover, expected);

        HandoverResponse response =
                service.confirm(new PickupConfirmationRequest("PICK-1", HandoverResult.collected), STAFF);

        assertThat(response).isEqualTo(expected);
        verify(handoverMutationService, never()).complete(any(), any(), any(), any());
    }

    @Test
    void rejectsAUsedCodeThatHasNoHandoverToReturn() {
        PickupCode pickupCode = activeCode();
        Parcel parcel = shelvedParcel();
        UUID parcelId = parcel.getId();
        UUID pickupCodeId = pickupCode.getId();
        when(pickupCode.getStatus()).thenReturn(PickupCodeStatus.used);
        when(pickupCodeRepository.findForUpdateByCode("PICK-1")).thenReturn(Optional.of(pickupCode));
        when(pickupCode.getParcelId()).thenReturn(parcelId);
        when(parcelRepository.findById(parcelId)).thenReturn(Optional.of(parcel));
        when(handoverRepository.findByPickupCodeId(pickupCodeId)).thenReturn(Optional.empty());

        assertThatThrownBy(
                        () -> service.confirm(new PickupConfirmationRequest("PICK-1", HandoverResult.collected), STAFF))
                .isInstanceOf(PickupCodeUsedException.class);
    }

    @Test
    void rejectsAResultOutsideTheSprintTwoCollectedValueBeforeReadingState() {
        assertThatThrownBy(
                        () -> service.confirm(new PickupConfirmationRequest("PICK-1", HandoverResult.returned), STAFF))
                .isInstanceOf(InvalidHandoverResultException.class);
        verify(pickupCodeRepository, never()).findForUpdateByCode(any());
    }

    @Test
    void rejectsAnUnknownCode() {
        when(pickupCodeRepository.findForUpdateByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                        service.confirm(new PickupConfirmationRequest("UNKNOWN", HandoverResult.collected), STAFF))
                .isInstanceOf(PickupCodeInvalidException.class);
    }

    private void givenActiveShelf(PickupCode pickupCode, Parcel parcel, Location location) {
        UUID parcelId = parcel.getId();
        UUID locationId = location.getId();
        when(pickupCodeRepository.findForUpdateByCode("PICK-1")).thenReturn(Optional.of(pickupCode));
        when(pickupCode.getParcelId()).thenReturn(parcelId);
        when(parcelRepository.findById(parcelId)).thenReturn(Optional.of(parcel));
        when(locationRepository.findByIdAndCollectionPointId(locationId, COLLECTION_POINT_ID))
                .thenReturn(Optional.of(location));
    }

    private static PickupCode activeCode() {
        PickupCode pickupCode = mock(PickupCode.class);
        when(pickupCode.getId()).thenReturn(UUID.randomUUID());
        when(pickupCode.getStatus()).thenReturn(PickupCodeStatus.active);
        return pickupCode;
    }

    private static Parcel shelvedParcel() {
        UUID parcelId = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        Parcel parcel = mock(Parcel.class);
        when(parcel.getId()).thenReturn(parcelId);
        when(parcel.getStatus()).thenReturn(ParcelStatus.shelved);
        when(parcel.getLocationId()).thenReturn(locationId);
        when(parcel.getCollectionPointId()).thenReturn(COLLECTION_POINT_ID);
        return parcel;
    }

    private static Location occupiedLocation(UUID locationId, UUID parcelId) {
        Location location = mock(Location.class);
        when(location.getId()).thenReturn(locationId);
        when(location.getStatus()).thenReturn(LocationStatus.occupied);
        when(location.getParcelId()).thenReturn(parcelId);
        return location;
    }

    private static void stubHandoverResponse(Handover handover, HandoverResponse response) {
        when(handover.getId()).thenReturn(response.id());
        when(handover.getParcelId()).thenReturn(response.parcelId());
        when(handover.getResult()).thenReturn(response.result());
        when(handover.getCreatedAt()).thenReturn(response.createdAt());
    }
}
