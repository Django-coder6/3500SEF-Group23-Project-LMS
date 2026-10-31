package com.group23.audit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Append-only persistence boundary for audit rows.
 *
 * <p>This repository deliberately exposes no update or delete operation. The
 * entity is also immutable, so the application has no code path that can
 * rewrite an audit entry.</p>
 */
@Repository
public class AuditLogRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UUID append(AuditLogEntry entry) {
        Objects.requireNonNull(entry, "entry must not be null");
        AuditLog log = AuditLog.from(entry);
        entityManager.persist(log);
        entityManager.flush();
        return log.getId();
    }
}
