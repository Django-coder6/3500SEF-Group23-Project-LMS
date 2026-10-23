package com.group23.common;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public final class RequestIds {

    private static final String ATTRIBUTE = RequestIds.class.getName();
    private static final String HEADER = "X-Request-Id";

    private RequestIds() {
    }

    public static String getOrCreate(HttpServletRequest request) {
        Object existing = request.getAttribute(ATTRIBUTE);
        if (existing instanceof String requestId) {
            return requestId;
        }

        String supplied = request.getHeader(HEADER);
        String requestId = supplied == null || supplied.isBlank()
                ? UUID.randomUUID().toString()
                : supplied;
        request.setAttribute(ATTRIBUTE, requestId);
        return requestId;
    }
}
