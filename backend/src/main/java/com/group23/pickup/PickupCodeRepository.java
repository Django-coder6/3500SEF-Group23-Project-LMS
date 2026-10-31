package com.group23.pickup;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Reads pickup codes and serializes confirmation by code. */
public interface PickupCodeRepository extends JpaRepository<PickupCode, UUID> {

    Optional<PickupCode> findByCode(String code);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from PickupCode c where c.code = :code")
    Optional<PickupCode> findForUpdateByCode(@Param("code") String code);
}
