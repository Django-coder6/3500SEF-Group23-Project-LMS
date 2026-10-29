package com.group23.parcel;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Persists and reads registered parcels. */
public interface ParcelRepository extends JpaRepository<Parcel, UUID> {}
