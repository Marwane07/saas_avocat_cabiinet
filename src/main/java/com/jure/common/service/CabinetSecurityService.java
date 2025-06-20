package com.jure.common.service;


import com.jure.common.persistant.model.Cabinet;
import com.jure.common.persistant.model.TenantContext;
import com.jure.common.persistant.model.User;
import com.jure.common.repository.CabinetRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CabinetSecurityService {

    private final CabinetRepository cabinetRepository;

    public CabinetSecurityService(CabinetRepository cabinetRepository) {
        this.cabinetRepository = cabinetRepository;
    }

    public boolean isCabinetManager(Long cabinetId) {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return false;
        }

        User user = (User) principal;

        // Vérifier si l'utilisateur est un manager de cabinet
        boolean isManager = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CABINET_MANAGER"));

        if (!isManager) {
            return false;
        }

        // Vérifier si l'utilisateur appartient au cabinet spécifié
        return user.getCabinet().getId().equals(cabinetId);
    }

    public boolean isCurrentCabinetManager() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            return false;
        }

        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId).orElse(null);
        if (cabinet == null) {
            return false;
        }

        return isCabinetManager(cabinet.getId());
    }
}