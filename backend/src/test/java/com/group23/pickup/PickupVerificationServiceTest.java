package com.group23.pickup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group23.location.Location;
import com.group23.location.LocationRepository;
import com.group23.location.LocationStatus;
import com.group23.parcel.Parcel;
import com.group23.parcel.ParcelRepository;
import com.group23.parcel.ParcelSize;
import com.group23.parcel.ParcelStatus;
import com.group23.security.AuthenticatedUser;
import com.group23.user.UserRole;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PickupVerificationServiceTest {

    private static final UUID STAFF_ID = UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID COLLECTION_POINT_ID = UUID.fromString("10000000-0000-4000-8000-000000000001");
    private static final UUID OTHER_COLLECTION_POINT_ID = UUID.fromString("10000000-0000-4000-8000-000000000002");
    private static final AuthenticatedUser STAFF = new AuthenticatedUser(STAFF_ID, UserRole.staff, COLLECTION_POINT_ID);

    private final PickupCodeRepository pickupCodeRepository = mock(PickupCodeRepository.class);
    private final ParcelRepository parcelRepository = mock(ParcelRepository.class);
    private final LocationRepository locationRepository = mock(LocationRepository.class);
    private final PickupVerificationService service =
            new PickupVerificationService(pickupCodeRepository, parcelRepository, locationRepository);

    @Test
    void verifiesAnActiveCodeAndReturnsTheShelvedParcelAtTheStaffCollectionPoint() {
        PickupCode pickupCode = activeCode();
        Parcel parcel = shelvedParcel(COLLECTION_POINT_ID);
        Location location = occupiedLocation(parcel.getLocationId(), parcel.getId(), COLLECTION_POINT_ID);
        UUID locationId = location.getId();
        givenCodeForParcel(pickupCode, parcel);
        when(locationRepository.findByIdAndCollectionPointId(locationId, COLLECTION_POINT_ID))
                .thenReturn(Optional.of(location));

        PickupVerifyResponse response = service.verify(new PickupVerifyRequest("PICK-1"), STAFF);

        assertThat(response.pickupCode()).isEqualTo("PICK-1");
        assertThat(response.parcelSummary().id()).isEqualTo(parcel.getId());
        assertThat(response.parcelSummary().recipientPhone()).isEqualTo("138****5678");
        assertThat(response.location().id()).isEqualTo(location.getId());
        assertThat(response.location().status()).isEqualTo(LocationStatus.occupied);
    }

    @Test
    void rejectsAnUnknownCode() {
        when(pickupCodeRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.verify(new PickupVerifyRequest("UNKNOWN"), STAFF))
                .isInstanceOf(PickupCodeInvalidException.class);
    }

    @Test
    void rejectsAnExpiredCode() {
        PickupCode pickupCode = activeCode();
        Parcel parcel = shelvedParcel(COLLECTION_POINT_ID);
        givenCodeForParcel(pickupCode, parcel);
        when(pickupCode.isExpiredAt(org.mockito.ArgumentMatchers.any(Instant.class)))
                .thenReturn(true);

        assertThatThrownBy(() -> service.verify(new PickupVerifyRequest("PICK-1"), STAFF))
                .isInstanceOf(PickupCodeExpiredException.class);
        verify(locationRepository, never())
                .findByIdAndCollectionPointId(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void rejectsAUsedCode() {
        PickupCode pickupCode = activeCode();
        Parcel parcel = shelvedParcel(COLLECTION_POINT_ID);
        givenCodeForParcel(pickupCode, parcel);
        when(pickupCode.getStatus()).thenReturn(PickupCodeStatus.used);

        assertThatThrownBy(() -> service.verify(new PickupVerifyRequest("PICK-1"), STAFF))
                .isInstanceOf(PickupCodeUsedException.class);
    }

    @Test
    void hidesWhetherACodeExistsAtAnotherCollectionPoint() {
        PickupCode pickupCode = activeCode();
        Parcel parcel = shelvedParcel(OTHER_COLLECTION_POINT_ID);
        givenCodeForParcel(pickupCode, parcel);

        assertThatThrownBy(() -> service.verify(new PickupVerifyRequest("PICK-1"), STAFF))
                .isInstanceOf(PickupCodeInvalidException.class);
        verify(locationRepository, never())
                .findByIdAndCollectionPointId(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void rejectsAParcelWhoseParcelAndLocationLinksDoNotMatch() {
        PickupCode pickupCode = activeCode();
        Parcel parcel = shelvedParcel(COLLECTION_POINT_ID);
        Location location = occupiedLocation(UUID.randomUUID(), parcel.getId(), COLLECTION_POINT_ID);
        UUID locationId = location.getId();
        givenCodeForParcel(pickupCode, parcel);
        when(locationRepository.findByIdAndCollectionPointId(locationId, COLLECTION_POINT_ID))
                .thenReturn(Optional.of(location));

        assertThatThrownBy(() -> service.verify(new PickupVerifyRequest("PICK-1"), STAFF))
                .isInstanceOf(ParcelNotOnShelfException.class);
    }

    private void givenCodeForParcel(PickupCode pickupCode, Parcel parcel) {
        UUID parcelId = parcel.getId();
        when(pickupCodeRepository.findByCode("PICK-1")).thenReturn(Optional.of(pickupCode));
        when(pickupCode.getParcelId()).thenReturn(parcelId);
        when(parcelRepository.findById(parcelId)).thenReturn(Optional.of(parcel));
    }

    private static PickupCode activeCode() {
        PickupCode pickupCode = mock(PickupCode.class);
        when(pickupCode.getStatus()).thenReturn(PickupCodeStatus.active);
        return pickupCode;
    }

    private static Parcel shelvedParcel(UUID collectionPointId) {
        UUID parcelId = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        Parcel parcel = mock(Parcel.class);
        when(parcel.getId()).thenReturn(parcelId);
        when(parcel.getTrackingNo()).thenReturn("TRACKING-1");
        when(parcel.getCarrier()).thenReturn("SF Express");
        when(parcel.getRecipientName()).thenReturn("Resident");
        when(parcel.getRecipientPhone()).thenReturn("13812345678");
        when(parcel.getSize()).thenReturn(ParcelSize.small);
        when(parcel.getStatus()).thenReturn(ParcelStatus.shelved);
        when(parcel.getLocationId()).thenReturn(locationId);
        when(parcel.getCollectionPointId()).thenReturn(collectionPointId);
        when(parcel.getCreatedAt()).thenReturn(Instant.parse("2026-10-30T00:00:00Z"));
        when(parcel.getUpdatedAt()).thenReturn(Instant.parse("2026-10-30T00:00:00Z"));
        return parcel;
    }

    private static Location occupiedLocation(UUID locationId, UUID parcelId, UUID collectionPointId) {
        Location location = mock(Location.class);
        when(location.getId()).thenReturn(locationId);
        when(location.getCode()).thenReturn("A-01-1");
        when(location.getZone()).thenReturn("A");
        when(location.getSize()).thenReturn(ParcelSize.small);
        when(location.getStatus()).thenReturn(LocationStatus.occupied);
        when(location.getParcelId()).thenReturn(parcelId);
        when(location.getCollectionPointId()).thenReturn(collectionPointId);
        return location;
    }
}
