package com.jure.common.service;


import com.jure.common.persistant.dto.AuthCabinetResponse;
import com.jure.common.persistant.dto.CabinetLoginRequest;
import com.jure.common.persistant.dto.JwtAuthenticationResponse;
import com.jure.common.persistant.dto.UserLoginRequest;
import com.jure.common.persistant.model.Cabinet;
import com.jure.common.persistant.model.User;
import com.jure.common.repository.CabinetRepository;
import com.jure.common.repository.UserRepository;
import com.jure.common.config.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

@Service
public class AuthService {

    private final CabinetRepository cabinetRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            CabinetRepository cabinetRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager) {
        this.cabinetRepository = cabinetRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthCabinetResponse authenticateCabinet(CabinetLoginRequest request) {
        Cabinet cabinet = cabinetRepository.findByNomCabinet(request.getNomCabinet())
                .orElseThrow(() -> new RuntimeException("Cabinet non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), cabinet.getPassword())) {
            throw new RuntimeException("Identifiants du cabinet invalides");
        }
        
        // Créer un token spécial pour le cabinet (tenant)
        Map<String, Object> claims = new HashMap<>();
        claims.put("tenantId", cabinet.getId().toString());
        claims.put("isTenant", true);
        
        String token = jwtUtil.createToken(claims, cabinet.getNomCabinet());
        Date expirationDate = jwtUtil.extractExpiration(token);
        
        return AuthCabinetResponse.builder()
                .token(token)
                .nomCabinet(cabinet.getNomCabinet())
                .cabinetId(cabinet.getId())
                .tokenType("Bearer")
                .expirationDate(expirationDate)
                .build();
    }

    public JwtAuthenticationResponse authenticateUser(UserLoginRequest request, String cabinetToken) {
        // Vérifier d'abord si le token du cabinet est valide
        if (cabinetToken == null || cabinetToken.isEmpty()) {
            throw new RuntimeException("Token de cabinet manquant");
        }
        
        // Extraire et vérifier les informations du token
        Long tenantId = jwtUtil.extractTenantId(cabinetToken);
        String nomCabinetFromToken = jwtUtil.extractUsername(cabinetToken);
        
        if (tenantId == null || nomCabinetFromToken == null) {
            throw new RuntimeException("Token de cabinet invalide");
        }
        
        // Vérifier si le token est expiré
        if (jwtUtil.isTokenExpired(cabinetToken)) {
            throw new RuntimeException("Token de cabinet expiré");
        }


        
        // Retrouver le cabinet par son ID extrait du token
        Long cabinetId = tenantId;
        Cabinet cabinet = cabinetRepository.findById(cabinetId)
                .orElseThrow(() -> new RuntimeException("Cabinet non trouvé"));
        
        // Vérifier que le nom du cabinet extrait du token correspond au nom du cabinet trouvé
        if (!cabinet.getNomCabinet().equals(nomCabinetFromToken)) {
            throw new RuntimeException("Les informations du token ne correspondent pas au cabinet");
        }
        
        // Vérifier que l'email n'est pas null
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new RuntimeException("Email manquant dans la requête");
        }
        
        // Utiliser AuthenticationManager pour authentifier l'utilisateur
        String usernameWithTenant = request.getEmail() + ":" + cabinet.getId();
        
        try {
            // Authentifier avec Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameWithTenant, request.getPassword())
            );
            
            // Si nous arrivons ici, l'authentification a réussi
            User utilisateur = (User) authentication.getPrincipal();
            
            // Générer le token JWT avec le tenantId
            String token = jwtUtil.generateToken(utilisateur, cabinet.getId().toString());
            
            // Créer une réponse enrichie avec les informations de l'utilisateur
            return JwtAuthenticationResponse.builder()
                .token(token)
                .id(utilisateur.getId())
                .email(utilisateur.getEmail())
                .nomComplet(utilisateur.getNomComplet())
                .telephone(utilisateur.getTelephone())
                .genre(utilisateur.getGenre())
                .role(utilisateur.getRole().getNom())
                .tokenType("Bearer")
                .build();
        } catch (Exception e) {
            // Log l'exception pour le débogage
            e.printStackTrace();
            throw new RuntimeException("Échec de l'authentification: " + e.getMessage());
        }
    }

    public AuthCabinetResponse validateCabinetToken(String token) {
        try {
            // Vérifier si le token est valide
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("Token manquant");
            }
            
            // Extraire les informations du token
            String nomCabinet = jwtUtil.extractUsername(token);
            Long tenantId = jwtUtil.extractTenantId(token);
            Date expirationDate = jwtUtil.extractExpiration(token);
            
            if (nomCabinet == null || tenantId == null) {
                throw new RuntimeException("Token invalide");
            }
            
            // Vérifier si le token est expiré
            if (jwtUtil.isTokenExpired(token)) {
                throw new RuntimeException("Token expiré");
            }
            
            // Retrouver le cabinet
            Long cabinetId = tenantId;
            Cabinet cabinet = cabinetRepository.findById(cabinetId)
                    .orElseThrow(() -> new RuntimeException("Cabinet non trouvé"));
            
            // Vérifier que le nom du cabinet correspond
            if (!cabinet.getNomCabinet().equals(nomCabinet)) {
                throw new RuntimeException("Les informations du token ne correspondent pas");
            }
            
            // Créer la réponse
            return AuthCabinetResponse.builder()
                    .token(token)
                    .nomCabinet(cabinet.getNomCabinet())
                    .cabinetId(cabinet.getId())
                    .tokenType("Bearer")
                    .expirationDate(expirationDate)
                    .build();
        } catch (Exception e) {
            // Log l'exception pour le débogage
            e.printStackTrace();
            throw new RuntimeException("Validation du token échouée: " + e.getMessage());
        }
    }
}
