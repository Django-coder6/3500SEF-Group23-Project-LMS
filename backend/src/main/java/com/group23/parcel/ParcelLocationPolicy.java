package com.group23.parcel;

import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/** Applies collection-point ownership checks to optional parcel location links. */
@Component
public class ParcelLocationPolicy {

    private final JdbcTemplate jdbcTemplate;

    public ParcelLocationPolicy(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean belongsToCollectionPoint(UUID locationId, UUID collectionPointId) {
        if (locationId == null) {
            return true;
        }
        Boolean exists = jdbcTemplate.queryForObject(
                """
                SELECT EXISTS (
                    SELECT 1
                    FROM locations
                    WHERE id = ? AND collection_point_id = ?
                )
                """,
                Boolean.class,
                locationId,
                collectionPointId);
        return Boolean.TRUE.equals(exists);
    }
}
