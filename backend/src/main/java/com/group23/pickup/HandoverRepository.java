package com.group23.pickup;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Reads and persists handover records. */
public interface HandoverRepository extends JpaRepository<Handover, UUID> {

    Optional<Handover> findByPickupCodeId(UUID pickupCodeId);
}
