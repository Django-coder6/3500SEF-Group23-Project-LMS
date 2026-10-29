package com.group23.parcel;

import com.group23.security.AuthenticatedUser;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/** Validates and registers batch entries independently, preserving partial success. */
@Service
public class ParcelBatchService {

    public static final int MAX_BATCH_SIZE = 50;

    private final ParcelRegistrationService registrationService;
    private final Validator validator;

    public ParcelBatchService(ParcelRegistrationService registrationService, Validator validator) {
        this.registrationService = registrationService;
        this.validator = validator;
    }

    public ParcelBatchResult register(ParcelBatchCreateRequest request, AuthenticatedUser actor) {
        ParcelRegistrationService.requireCollectionPoint(actor);
        if (request == null || request.parcels() == null || request.parcels().isEmpty()) {
            throw new InvalidParcelBatchException();
        }
        if (request.parcels().size() > MAX_BATCH_SIZE) {
            throw new BatchLimitExceededException();
        }

        List<ParcelResponse> created = new ArrayList<>();
        List<ParcelBatchRejection> rejected = new ArrayList<>();
        for (int index = 0; index < request.parcels().size(); index++) {
            ParcelCreateRequest parcel = request.parcels().get(index);
            if (parcel == null) {
                rejected.add(new ParcelBatchRejection(index, null, "parcel must not be null"));
                continue;
            }

            Set<ConstraintViolation<ParcelCreateRequest>> violations = validator.validate(parcel);
            if (!violations.isEmpty()) {
                rejected.add(new ParcelBatchRejection(index, parcel.trackingNo(), describe(violations)));
                continue;
            }

            try {
                created.add(registrationService.register(parcel, actor));
            } catch (DuplicateTrackingNoException exception) {
                rejected.add(new ParcelBatchRejection(
                        index, parcel.trackingNo(), "trackingNo already exists; use a different tracking number"));
            } catch (ParcelRegistrationException exception) {
                rejected.add(new ParcelBatchRejection(index, parcel.trackingNo(), exception.getMessage()));
            }
        }
        return new ParcelBatchResult(List.copyOf(created), List.copyOf(rejected));
    }

    private static String describe(Set<ConstraintViolation<ParcelCreateRequest>> violations) {
        return violations.stream()
                .sorted(Comparator.comparing(
                        violation -> violation.getPropertyPath().toString()))
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining("; "));
    }
}
