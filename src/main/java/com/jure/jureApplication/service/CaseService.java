package com.jure.jureApplication.service;

import com.jure.common.annotation.MultitenantSearchMethod;
import com.jure.common.exception.ResourceNotFoundException;
import com.jure.common.persistant.model.Cabinet;
import com.jure.common.persistant.model.TenantContext;
import com.jure.common.persistant.model.User;
import com.jure.common.repository.CabinetRepository;
import com.jure.common.repository.UserRepository;
import com.jure.jureApplication.persistent.dto.CaseCriteria;
import com.jure.jureApplication.persistent.dto.CaseDTO;
import com.jure.jureApplication.mapper.CaseMapper;
import com.jure.jureApplication.persistent.model.Case;
import com.jure.jureApplication.repository.CaseCriteriaRepository;
import com.jure.jureApplication.repository.CaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CaseService {

    private final CaseRepository caseRepository;
    private final CaseCriteriaRepository caseCriteriaRepository;
    private final CabinetRepository cabinetRepository;
    private final UserRepository userRepository;
    private final CaseMapper caseMapper;

    public CaseService(
            CaseRepository caseRepository,
            CaseCriteriaRepository caseCriteriaRepository,
            CabinetRepository cabinetRepository,
            UserRepository userRepository,
            CaseMapper caseMapper) {
        this.caseRepository = caseRepository;
        this.caseCriteriaRepository = caseCriteriaRepository;
        this.cabinetRepository = cabinetRepository;
        this.userRepository = userRepository;
        this.caseMapper = caseMapper;
    }

    @Transactional
    @MultitenantSearchMethod(description = "Création d'un nouveau dossier")
    public Case createCase(CaseDTO request) {
        Long tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        String reference = "Ref-" + System.currentTimeMillis(); // Générer une référence unique
        // Créer le dossier
        Case caseEntity = new Case();
        caseEntity.setReference(reference);
        caseEntity.setTitle(request.getTitle());
        caseEntity.setDescription(request.getDescription());
        caseEntity.setCourtDate(request.getCourtDate());
        caseEntity.setStatus(request.getStatus());
        caseEntity.setClientName(request.getClientName());
        caseEntity.setClientContact(request.getClientContact());
        caseEntity.setCabinet(cabinet);

        // Assigner l'utilisateur si spécifié
        if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findByIdAndCabinet_Id(request.getAssignedUserId(), cabinet.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssignedUserId()));
            caseEntity.setAssignedUser(assignedUser);
        }

        // Sauvegarder le dossier
        caseEntity = caseRepository.save(caseEntity);

        return caseEntity;
    }

    @MultitenantSearchMethod(description = "Récupération de tous les dossiers du cabinet")
    public List<CaseDTO> getAllCases() {
        // Récupérer le cabinet actuel à partir du contexte du tenant
        Long tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        // Récupérer tous les dossiers du cabinet
        List<Case> cases = caseRepository.findByCabinet_Id(cabinet.getId());

        return caseMapper.casesToDtos(cases);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CABINET_MANAGER', 'ROLE_AVOCAT', 'ROLE_ASSISTANT')")
    @MultitenantSearchMethod(description = "Récupération d'un dossier par ID")
    public Case getCaseById(Long caseId) {
        // Récupérer le cabinet actuel à partir du contexte du tenant
        Long tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        // Récupérer le dossier par ID et s'assurer qu'il appartient au cabinet
        Case caseEntity = caseRepository.findByIdAndCabinet_Id(caseId, cabinet.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Case", "id", caseId));

        return caseEntity;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CABINET_MANAGER', 'ROLE_AVOCAT')")
    @Transactional
    @MultitenantSearchMethod(description = "Mise à jour d'un dossier")
    public CaseDTO updateCase(Long caseId, CaseDTO  request) {
        // Récupérer le cabinet actuel à partir du contexte du tenant
        Long tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        // Récupérer le dossier par ID et s'assurer qu'il appartient au cabinet
        Case caseEntity = caseRepository.findByIdAndCabinet_Id(caseId, cabinet.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Case", "id", caseId));

        // Mettre à jour les champs
        caseEntity.setReference(request.getReference());
        caseEntity.setTitle(request.getTitle());
        caseEntity.setDescription(request.getDescription());
        caseEntity.setCourtDate(request.getCourtDate());
        caseEntity.setStatus(request.getStatus());
        caseEntity.setClientName(request.getClientName());
        caseEntity.setClientContact(request.getClientContact());

        // Mettre à jour l'utilisateur assigné si spécifié
        if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findByIdAndCabinet_Id(request.getAssignedUserId(), cabinet.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssignedUserId()));
            caseEntity.setAssignedUser(assignedUser);
        } else {
            caseEntity.setAssignedUser(null);
        }

        // Sauvegarder les modifications
        caseEntity = caseRepository.save(caseEntity);

        return caseMapper.caseToDto(caseEntity);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CABINET_MANAGER', 'ROLE_AVOCAT')")
    @Transactional
    @MultitenantSearchMethod(description = "Suppression d'un dossier")
    public void deleteCase(Long caseId) {
        // Récupérer le cabinet actuel à partir du contexte du tenant
        Long tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        // Récupérer le dossier par ID et s'assurer qu'il appartient au cabinet
        Case caseEntity = caseRepository.findByIdAndCabinet_Id(caseId, cabinet.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Case", "id", caseId));

        // Supprimer le dossier
        caseRepository.delete(caseEntity);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CABINET_MANAGER', 'ROLE_AVOCAT', 'ROLE_ASSISTANT')")
    @MultitenantSearchMethod(description = "Recherche de dossiers par critères")
    public Page<Case> searchCases(CaseCriteria criteria, Pageable pageable) {
        // Récupérer le cabinet actuel à partir du contexte du tenant
        Long tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        // Rechercher les dossiers avec les critères et le cabinet
        Page<Case> casesPage = caseCriteriaRepository.findByCriteria(criteria, cabinet.getId(), pageable);

        // Convertir les résultats en DTOs
        return casesPage;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CABINET_MANAGER', 'ROLE_AVOCAT', 'ROLE_ASSISTANT')")
    @MultitenantSearchMethod(description = "Recherche de dossiers par critères sans pagination")
    public Page<Case> searchCasesNotPaged(CaseCriteria criteria) {
        // Récupérer le cabinet actuel à partir du contexte du tenant
        Long tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        // Rechercher les dossiers avec les critères et le cabinet
        Page<Case> casesPage = caseCriteriaRepository.findByCriteriaNotPaged(criteria, cabinet.getId());

        // Convertir les résultats en DTOs
        return casesPage;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CABINET_MANAGER', 'ROLE_AVOCAT', 'ROLE_ASSISTANT')")
    @MultitenantSearchMethod(description = "Récupération des dossiers assignés à un utilisateur")
    public List<Case> getCasesByAssignedUser(Long userId) {
        // Récupérer le cabinet actuel à partir du contexte du tenant
        Long tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        // Vérifier que l'utilisateur appartient au cabinet
        userRepository.findByIdAndCabinet_Id(userId, cabinet.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Récupérer les dossiers assignés à l'utilisateur dans ce cabinet
        List<Case> cases = caseRepository.findByAssignedUser_IdAndCabinet_Id(userId, cabinet.getId());

        return cases;
    }


   /* @PreAuthorize("hasAnyAuthority('ROLE_CABINET_MANAGER', 'ROLE_AVOCAT', 'ROLE_ASSISTANT')")
    @MultitenantSearchMethod(description = "Recherche de dossiers par critères")
    public Page<CaseDTO> searchCases(CaseCriteria criteria, Pageable pageable) {
        // Récupérer le cabinet actuel à partir du contexte du tenant
        String tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        // Rechercher les dossiers avec les critères et le cabinet
        Page<Case> casesPage = caseCriteriaRepository.findByCriteria(criteria, cabinet.getId(), pageable);

        // Convertir les résultats en DTOs
        return casesPage.map(caseMapper::caseToDto);
    }*/

 /*   @PreAuthorize("hasAnyAuthority('ROLE_CABINET_MANAGER', 'ROLE_AVOCAT', 'ROLE_ASSISTANT')")
    @MultitenantSearchMethod(description = "Recherche de dossiers par critères sans pagination")
    public List<CaseDTO> searchCasesNotPaged(CaseCriteria criteria) {
        // Récupérer le cabinet actuel à partir du contexte du tenant
        String tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        // Rechercher les dossiers avec les critères et le cabinet
        Page<Case> casesPage = caseCriteriaRepository.findByCriteriaNotPaged(criteria, cabinet.getId());

        // Convertir les résultats en DTOs
        return casesPage.getContent().stream()
                .map(caseMapper::caseToDto)
                .toList();
    }*/

}