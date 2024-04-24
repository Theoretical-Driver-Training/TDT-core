package com.example.api.controller;

import com.example.api.payload.request.SigninRequest;
import com.example.api.payload.request.SignupRequest;
import com.example.api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthService authService;

    public AuthenticationController(AuthService service) {
        this.authService = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        return authService.registerUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody SigninRequest request) {
        return authService.authenticateUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String bearerToken) {
        return authService.logoutUser(bearerToken);
    }
}
