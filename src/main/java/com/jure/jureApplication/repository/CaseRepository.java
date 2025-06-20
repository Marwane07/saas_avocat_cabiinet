package com.jure.jureApplication.repository;

import com.jure.jureApplication.persistent.model.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
    List<Case> findByAssignedUser_IdAndCabinet_Id(Long userId, Long id);

    Optional<Case> findByIdAndCabinet_Id(Long caseId, Long id);

    List<Case> findByCabinet_Id(Long id);
}
