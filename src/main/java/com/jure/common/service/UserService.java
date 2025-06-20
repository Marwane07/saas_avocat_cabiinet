package com.jure.common.service;


import com.jure.common.exception.ResourceNotFoundException;
import com.jure.common.persistant.dto.UserCreationRequest;
import com.jure.common.persistant.dto.UserResponse;
import com.jure.common.persistant.model.Cabinet;
import com.jure.common.persistant.model.Role;
import com.jure.common.persistant.model.TenantContext;
import com.jure.common.persistant.model.User;
import com.jure.common.repository.CabinetRepository;
import com.jure.common.repository.RoleRepository;
import com.jure.common.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CabinetRepository cabinetRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            CabinetRepository cabinetRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cabinetRepository = cabinetRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAuthority('ROLE_CABINET_MANAGER')")
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        // Récupérer le cabinet actuel à partir du contexte du tenant
        Long tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        // Vérifier si l'email existe déjà dans ce cabinet
        if (userRepository.findByEmailAndCabinet_Id(request.getEmail(), cabinet.getId()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà dans ce cabinet");
        }

        // Récupérer le rôle
        Role role = roleRepository.findByNom(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "nom", request.getRole()));

        // Créer l'utilisateur
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNomComplet(request.getNomComplet());
        user.setTelephone(request.getTelephone());
        user.setGenre(request.getGenre());
        user.setRole(role);
        user.setCabinet(cabinet);

        // Sauvegarder l'utilisateur
        user = userRepository.save(user);

        return mapToResponse(user);
    }

    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN') or @cabinetSecurityService.isCabinetManager(#cabinetId)")
    public List<UserResponse> getUsersByCabinetId(Long cabinetId) {
        return userRepository.findByCabinet_Id(cabinetId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public List<UserResponse> getCurrentCabinetUsers() {
        Long tenantId = TenantContext.getCurrentTenant();
        Cabinet cabinet = cabinetRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Cabinet", "tenantId", tenantId));

        return userRepository.findByCabinet_Id(cabinet.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setNomComplet(user.getNomComplet());
        response.setTelephone(user.getTelephone());
        response.setGenre(user.getGenre());
        response.setRole(user.getRole().getNom());
        return response;
    }
}