package com.jure.common.controller;


import com.jure.common.persistant.dto.UserCreationRequest;
import com.jure.common.persistant.dto.UserResponse;
import com.jure.common.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreationRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cabinet/{cabinetId}")
    public ResponseEntity<List<UserResponse>> getUsersByCabinetId(@PathVariable Long cabinetId) {
        List<UserResponse> users = userService.getUsersByCabinetId(cabinetId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/current-cabinet-users")
    public ResponseEntity<List<UserResponse>> getCurrentCabinetUsers() {
        List<UserResponse> users = userService.getCurrentCabinetUsers();
        return ResponseEntity.ok(users);
    }
}