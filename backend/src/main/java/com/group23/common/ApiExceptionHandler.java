package com.group23.common;

import com.group23.auth.InvalidCredentialsException;
import com.group23.parcel.BatchLimitExceededException;
import com.group23.parcel.DuplicateTrackingNoException;
import com.group23.parcel.InvalidParcelBatchException;
import com.group23.parcel.MissingCollectionPointException;
import com.group23.parcel.ParcelRegistrationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Converts application exceptions into the shared API error response. */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> invalidCredentials(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiErrorResponse.of(
                        "INVALID_CREDENTIALS", "Invalid username or password.", RequestIds.getOrCreate(request)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> invalidRequest(
            HttpServletRequest request, MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> "The field '" + error.getField() + "' " + validationReason(error.getCode())
                        + ". Correct it and submit the request again.")
                .orElse("Correct the invalid request fields and submit the request again.");
        return ResponseEntity.badRequest()
                .body(ApiErrorResponse.of("VALIDATION_ERROR", message, RequestIds.getOrCreate(request)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> unreadableRequest(HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(ApiErrorResponse.of(
                        "VALIDATION_ERROR",
                        "Check that every field matches the API contract's required type and allowed values, then retry.",
                        RequestIds.getOrCreate(request)));
    }

    @ExceptionHandler(DuplicateTrackingNoException.class)
    public ResponseEntity<ApiErrorResponse> duplicateTrackingNo(
            HttpServletRequest request, DuplicateTrackingNoException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiErrorResponse.of(
                        "DUPLICATE_TRACKING_NO", exception.getMessage(), RequestIds.getOrCreate(request)));
    }

    @ExceptionHandler(BatchLimitExceededException.class)
    public ResponseEntity<ApiErrorResponse> batchLimitExceeded(
            HttpServletRequest request, BatchLimitExceededException exception) {
        return ResponseEntity.badRequest()
                .body(ApiErrorResponse.of(
                        "BATCH_LIMIT_EXCEEDED", exception.getMessage(), RequestIds.getOrCreate(request)));
    }

    @ExceptionHandler(InvalidParcelBatchException.class)
    public ResponseEntity<ApiErrorResponse> invalidBatch(
            HttpServletRequest request, InvalidParcelBatchException exception) {
        return ResponseEntity.badRequest()
                .body(ApiErrorResponse.of("VALIDATION_ERROR", exception.getMessage(), RequestIds.getOrCreate(request)));
    }

    @ExceptionHandler(ParcelRegistrationException.class)
    public ResponseEntity<ApiErrorResponse> invalidParcel(
            HttpServletRequest request, ParcelRegistrationException exception) {
        return ResponseEntity.badRequest()
                .body(ApiErrorResponse.of("VALIDATION_ERROR", exception.getMessage(), RequestIds.getOrCreate(request)));
    }

    @ExceptionHandler(MissingCollectionPointException.class)
    public ResponseEntity<ApiErrorResponse> missingCollectionPoint(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiErrorResponse.of(
                        "FORBIDDEN",
                        "Ask an administrator to assign this staff account to a collection point, then sign in again.",
                        RequestIds.getOrCreate(request)));
    }

    private static String validationReason(String code) {
        return switch (code == null ? "" : code) {
            case "NotBlank" -> "must not be blank";
            case "NotEmpty" -> "must not be empty";
            case "NotNull" -> "must be provided";
            case "Size" -> "is outside the allowed length";
            default -> "is invalid";
        };
    }
}
