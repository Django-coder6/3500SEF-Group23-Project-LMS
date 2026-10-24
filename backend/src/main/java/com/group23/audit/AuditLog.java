package com.group23.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    private UUID id;

    @Column(name = "actor_id", nullable = false, updatable = false)
    private UUID actorId;

    @Column(nullable = false, updatable = false, length = 100)
    private String action;

    @Column(name = "target_type", nullable = false, updatable = false, length = 100)
    private String targetType;

    @Column(name = "target_id", nullable = false, updatable = false)
    private UUID targetId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, updatable = false, columnDefinition = "jsonb")
    private Map<String, Object> before;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, updatable = false, columnDefinition = "jsonb")
    private Map<String, Object> after;

    @Column(name = "collection_point_id", nullable = false, updatable = false)
    private UUID collectionPointId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected AuditLog() {
    }

    private AuditLog(AuditLogEntry entry) {
        this.id = UUID.randomUUID();
        this.actorId = entry.actorId();
        this.action = entry.action();
        this.targetType = entry.targetType();
        this.targetId = entry.targetId();
        this.before = entry.before();
        this.after = entry.after();
        this.collectionPointId = entry.collectionPointId();
        this.createdAt = Instant.now();
    }

    static AuditLog from(AuditLogEntry entry) {
        return new AuditLog(entry);
    }

    UUID getId() {
        return id;
    }
}
