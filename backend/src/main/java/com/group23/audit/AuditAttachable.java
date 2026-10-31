package com.group23.audit;

import java.util.UUID;

/**
 * Allows an audited operation to finish a dependent write after the audit row
 * has received its generated identifier.
 */
public interface AuditAttachable {

    Object attachAudit(UUID auditId);
}
