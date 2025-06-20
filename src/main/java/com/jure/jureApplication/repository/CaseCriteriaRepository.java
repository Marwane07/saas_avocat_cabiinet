package com.jure.jureApplication.repository;


import com.jure.jureApplication.persistent.dto.CaseCriteria;
import com.jure.jureApplication.persistent.model.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CaseCriteriaRepository {
    Page<Case> findByCriteria(CaseCriteria criteria, Long cabinetId, Pageable pageable);
    Page<Case> findByCriteriaNotPaged(CaseCriteria criteria, Long cabinetId);
}