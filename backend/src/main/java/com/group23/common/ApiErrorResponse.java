package com.group23.common;

public record ApiErrorResponse(ApiError error) {

    public static ApiErrorResponse of(String code, String message, String requestId) {
        return new ApiErrorResponse(new ApiError(code, message, requestId));
    }

    public record ApiError(String code, String message, String requestId) {
    }
}
