package com.group23.audit;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record AuditLogEntry(
        UUID actorId,
        String action,
        String targetType,
        UUID targetId,
        Map<String, Object> before,
        Map<String, Object> after,
        UUID collectionPointId) {

    public AuditLogEntry {
        Objects.requireNonNull(actorId, "actorId must not be null");
        action = requireText(action, "action");
        targetType = requireText(targetType, "targetType");
        Objects.requireNonNull(targetId, "targetId must not be null");
        Objects.requireNonNull(before, "before must not be null");
        Objects.requireNonNull(after, "after must not be null");
        Objects.requireNonNull(collectionPointId, "collectionPointId must not be null");
    }

    private static String requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value.trim();
    }
}
