package com.jure.common.repository;

import com.jure.common.persistant.model.Cabinet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CabinetRepository extends JpaRepository<Cabinet, Long> {
    Optional<Cabinet> findByNomCabinet(String nomCabinet);
    Optional<Cabinet> findByTenantId(Long tenantId);
}