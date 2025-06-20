
package com.jure.common.service;
import com.jure.common.exception.ResourceNotFoundException;
import com.jure.common.persistant.dto.CabinetCreationRequest;
import com.jure.common.persistant.dto.CabinetResponse;
import com.jure.common.persistant.model.Cabinet;
import com.jure.common.repository.CabinetRepository;
import com.jure.common.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CabinetService {

    private final CabinetRepository cabinetRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CabinetService(
            CabinetRepository cabinetRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.cabinetRepository = cabinetRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @Transactional
    public CabinetResponse createCabinet(CabinetCreationRequest request) {
        // Vérifier si le nom du cabinet existe déjà
        if (cabinetRepository.findByNomCabinet(request.getNomCabinet()).isPresent()) {
            throw new IllegalArgumentException("Un cabinet avec ce nom existe déjà");
        }


        Long countMax = cabinetRepository.count();
        // Créer le cabinet
        Cabinet cabinet = new Cabinet();
        cabinet.setNomCabinet(request.getNomCabinet());
        cabinet.setPassword(passwordEncoder.encode(request.getPassword()));

        cabinet.setTenantId(countMax+1);

        // Sauvegarder le cabinet
        cabinet = cabinetRepository.save(cabinet);

        return mapToResponse(cabinet);
    }

    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public List<CabinetResponse> getAllCabinets() {
        return cabinetRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN') or @cabinetSecurityService.isCabinetManager(#cabinetId)")
    public CabinetResponse getCabinetById(Long cabinetId) {
        Cabinet cabinet = cabinetRepository.findById(cabinetId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "id", cabinetId));
        return mapToResponse(cabinet);
    }

    private CabinetResponse mapToResponse(Cabinet cabinet) {
        CabinetResponse response = new CabinetResponse();
        response.setId(cabinet.getId());
        response.setNomCabinet(cabinet.getNomCabinet());
        response.setTenantId(cabinet.getTenantId());

        // Compter le nombre d'utilisateurs dans ce cabinet
        long userCount = userRepository.countByCabinet_Id(cabinet.getId());
        response.setNombreUtilisateurs(userCount);

        return response;
    }
}