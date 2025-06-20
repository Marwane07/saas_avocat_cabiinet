package com.jure.common.repository;

import com.jure.common.persistant.model.User;
import jakarta.validation.ReportAsSingleViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndCabinet_Id(String email, Long cabinetId);
    List<User> findByCabinet_Id(Long cabinetId);
    long countByCabinet_Id(Long cabinetId);
    Optional<User> findByIdAndCabinet_Id(Long assignedUserId, Long id);
}