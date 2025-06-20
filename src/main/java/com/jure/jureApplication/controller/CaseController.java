package com.jure.jureApplication.controller;

import com.jure.jureApplication.mapper.CaseMapper;
import com.jure.jureApplication.persistent.dto.CaseCriteria;
import com.jure.jureApplication.persistent.dto.CaseDTO;
import com.jure.jureApplication.persistent.model.Case;
import com.jure.jureApplication.service.CaseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService caseService;

    @Autowired
    private  CaseMapper caseMapper;


    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @PostMapping("/create")
    public ResponseEntity<CaseDTO> createCase(@Valid @RequestBody CaseDTO request) {
        Case createdCase = caseService.createCase(request);
        CaseDTO createdCaseDTO = caseMapper.caseToDto(createdCase);
        return new ResponseEntity<>(createdCaseDTO, HttpStatus.CREATED);
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<CaseDTO> getCaseById(@PathVariable("id") Long caseId) {
        Case caseCreated = caseService.getCaseById(caseId);
        CaseDTO caseDTO = caseMapper.caseToDto(caseCreated);
        return ResponseEntity.ok(caseDTO);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable("id") Long caseId) {
        caseService.deleteCase(caseId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CaseDTO>> getCasesByAssignedUser(@PathVariable("userId") Long userId) {
        List<Case> cases = caseService.getCasesByAssignedUser(userId);
        List<CaseDTO> caseDTOs = caseMapper.casesToDtos(cases);
        return ResponseEntity.ok(caseDTOs);
    }


    @PostMapping("/search-paged")
    public ResponseEntity<Page<CaseDTO>> searchCasesPaged(@RequestBody CaseCriteria criteria, Pageable pageable) {
        Page<Case> cases = caseService.searchCases(criteria, pageable);
        Page<CaseDTO> caseDTOs = cases.map(caseMapper::caseToDto);
        return ResponseEntity.ok(caseDTOs);
    }

    @PostMapping("/search-all")
    public ResponseEntity<List<CaseDTO>> searchCasesAll(@RequestBody CaseCriteria criteria) {
        Page<Case> cases = caseService.searchCasesNotPaged(criteria);
        List<CaseDTO> caseDTOs = cases.getContent().stream()
                .map(caseMapper::caseToDto).collect(Collectors.toList());
        return ResponseEntity.ok(caseDTOs);
    }
}