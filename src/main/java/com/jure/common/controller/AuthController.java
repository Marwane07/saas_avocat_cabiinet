package com.jure.common.controller;



import com.jure.common.persistant.dto.AuthCabinetResponse;
import com.jure.common.persistant.dto.CabinetLoginRequest;
import com.jure.common.persistant.dto.JwtAuthenticationResponse;
import com.jure.common.persistant.dto.UserLoginRequest;
import com.jure.common.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/cabinet-login")
    public ResponseEntity<AuthCabinetResponse> authenticateCabinet(@RequestBody CabinetLoginRequest request) {
        AuthCabinetResponse response = authService.authenticateCabinet(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(
            @RequestBody UserLoginRequest request,
            @RequestHeader("Cabinet-Authorization") String cabinetToken) {
        // Vérifier si le token commence par "Bearer "
        if (cabinetToken.startsWith("Bearer ")) {
            cabinetToken = cabinetToken.substring(7);
        }
        
        JwtAuthenticationResponse response = authService.authenticateUser(request, cabinetToken);
        return ResponseEntity.ok(response);
    }
}
