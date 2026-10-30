package com.group23.location;

import jakarta.validation.constraints.NotNull;

/** Request body for a staff-initiated location state change. */
public record LocationStatusUpdateRequest(@NotNull(message = "must be provided") LocationStatus status) {}
