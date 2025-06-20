package com.jure.common.service;


import com.jure.common.persistant.model.User;
import com.jure.common.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameWithTenant) throws UsernameNotFoundException {
        // Le format attendu est "email:cabinetId"
        String[] parts = usernameWithTenant.split(":");
        if (parts.length != 2) {
            throw new UsernameNotFoundException("Format de nom d'utilisateur invalide");
        }

        String email = parts[0];
        Long cabinetId;
        try {
            cabinetId = Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("ID de cabinet invalide");
        }

        // Rechercher l'utilisateur avec l'email et le cabinetId
        User user = userRepository.findByEmailAndCabinet_Id(email, cabinetId)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        return user;
    }
}