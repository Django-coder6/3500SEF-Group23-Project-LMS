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
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class ParcelBatchServiceTest {

    private static final UUID STAFF_ID = UUID.fromString("20000000-0000-4000-8000-000000000002");
    private static final UUID COLLECTION_POINT_ID = UUID.fromString("10000000-0000-4000-8000-000000000001");
    private static final AuthenticatedUser STAFF = new AuthenticatedUser(STAFF_ID, UserRole.staff, COLLECTION_POINT_ID);
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    private final ParcelRegistrationService registrationService = mock(ParcelRegistrationService.class);
    private final ParcelBatchService service = new ParcelBatchService(registrationService, VALIDATOR);

    @AfterAll
    static void closeValidatorFactory() {
        VALIDATOR_FACTORY.close();
    }

    @Test
    void rejectsMoreThanFiftyEntriesBeforeRegistration() {
        List<ParcelCreateRequest> parcels =
                IntStream.range(0, 51).mapToObj(index -> request("SF-" + index)).toList();

        assertThatThrownBy(() -> service.register(new ParcelBatchCreateRequest(parcels), STAFF))
                .isInstanceOf(BatchLimitExceededException.class);
        verify(registrationService, never()).register(any(), any());
    }

    @Test
    void acceptsTheMaximumOfFiftyEntries() {
        when(registrationService.register(any(ParcelCreateRequest.class), any(AuthenticatedUser.class)))
                .thenAnswer(invocation -> response(invocation.getArgument(0)));
        List<ParcelCreateRequest> parcels =
                IntStream.range(0, 50).mapToObj(index -> request("SF-" + index)).toList();

        ParcelBatchResult result = service.register(new ParcelBatchCreateRequest(parcels), STAFF);

        assertThat(result.created()).hasSize(50);
        assertThat(result.rejected()).isEmpty();
    }

    @Test
    void validEntryIsCreatedWhileInvalidAndDuplicateEntriesAreRejected() {
        ParcelCreateRequest valid = request("SF-2001");
        ParcelCreateRequest invalid =
                new ParcelCreateRequest("", "SF Express", "Recipient", "13812345678", ParcelSize.small, null);
        ParcelCreateRequest duplicate = request("SF-2002");
        when(registrationService.register(valid, STAFF)).thenReturn(response(valid));
        when(registrationService.register(duplicate, STAFF)).thenThrow(new DuplicateTrackingNoException());

        ParcelBatchResult result =
                service.register(new ParcelBatchCreateRequest(List.of(valid, invalid, duplicate)), STAFF);

        assertThat(result.created()).extracting(ParcelResponse::trackingNo).containsExactly("SF-2001");
        assertThat(result.rejected()).extracting(ParcelBatchRejection::index).containsExactly(1, 2);
        assertThat(result.rejected().get(0).reason()).contains("trackingNo");
        assertThat(result.rejected().get(1).reason()).contains("already exists");
    }

    private static ParcelResponse response(ParcelCreateRequest request) {
        return new ParcelResponse(
                UUID.randomUUID(),
                request.trackingNo(),
                request.carrier(),
                request.recipientName(),
                request.recipientPhone(),
                ParcelStatus.registered,
                request.size(),
                request.locationId(),
                0,
                Instant.parse("2026-10-29T00:00:00Z"),
                Instant.parse("2026-10-29T00:00:00Z"));
    }

    private static ParcelCreateRequest request(String trackingNo) {
        return new ParcelCreateRequest(trackingNo, "SF Express", "Recipient", "13812345678", ParcelSize.small, null);
    }
}
