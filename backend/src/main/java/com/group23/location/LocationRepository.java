package com.group23.location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Reads storage locations within one collection point. */
public interface LocationRepository extends JpaRepository<Location, UUID> {

    List<Location> findAllByCollectionPointIdOrderByZoneAscCodeAsc(UUID collectionPointId);

    Optional<Location> findByIdAndCollectionPointId(UUID id, UUID collectionPointId);
}
