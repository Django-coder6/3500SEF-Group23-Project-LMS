package com.group23.parcel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group23.security.AuthenticatedUser;
import com.group23.user.UserRole;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class ParcelRegistrationServiceTest {

    private static final UUID STAFF_ID = UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID COLLECTION_POINT_ID = UUID.fromString("10000000-0000-4000-8000-000000000001");
    private static final AuthenticatedUser STAFF = new AuthenticatedUser(STAFF_ID, UserRole.staff, COLLECTION_POINT_ID);

    private final ParcelRepository parcelRepository = mock(ParcelRepository.class);
    private final ParcelLocationPolicy locationPolicy = mock(ParcelLocationPolicy.class);
    private final ParcelRegistrationService service = new ParcelRegistrationService(parcelRepository, locationPolicy);

    @Test
    void registersParcelInTheAuthenticatedStaffCollectionPoint() {
        when(parcelRepository.saveAndFlush(any(Parcel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParcelResponse response = service.register(request("SF-1001"), STAFF);

        assertThat(response.trackingNo()).isEqualTo("SF-1001");
        assertThat(response.recipientPhone()).isEqualTo("138****5678");
        assertThat(response.status()).isEqualTo(ParcelStatus.registered);
        assertThat(response.expectedVersion()).isZero();
        verify(parcelRepository).saveAndFlush(any(Parcel.class));
    }

    @Test
    void translatesTheDatabaseUniqueConstraintIntoADuplicateError() {
        when(parcelRepository.saveAndFlush(any(Parcel.class)))
                .thenThrow(new DataIntegrityViolationException(
                        "duplicate key violates unique constraint \"uq_parcels_tracking_no\""));

        assertThatThrownBy(() -> service.register(request("SF-1001"), STAFF))
                .isInstanceOf(DuplicateTrackingNoException.class);
    }

    @Test
    void rejectsStaffWithoutACollectionPoint() {
        AuthenticatedUser unassigned = new AuthenticatedUser(STAFF_ID, UserRole.staff, null);

        assertThatThrownBy(() -> service.register(request("SF-1001"), unassigned))
                .isInstanceOf(MissingCollectionPointException.class);
    }

    @Test
    void rejectsALocationFromAnotherCollectionPointBeforeSaving() {
        UUID locationId = UUID.randomUUID();
        ParcelCreateRequest request = new ParcelCreateRequest(
                "SF-1001", "SF Express", "Recipient", "13812345678", ParcelSize.small, locationId);
        when(locationPolicy.belongsToCollectionPoint(locationId, COLLECTION_POINT_ID))
                .thenReturn(false);

        assertThatThrownBy(() -> service.register(request, STAFF))
                .isInstanceOf(ParcelRegistrationException.class)
                .hasMessageContaining("from your collection point");
        verify(parcelRepository, never()).saveAndFlush(any(Parcel.class));
    }

    private static ParcelCreateRequest request(String trackingNo) {
        return new ParcelCreateRequest(trackingNo, "SF Express", "Recipient", "13812345678", ParcelSize.small, null);
    }
}
