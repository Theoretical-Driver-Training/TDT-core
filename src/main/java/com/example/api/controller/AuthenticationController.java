package com.example.api.controller;

import com.example.api.payload.request.AuthRequest;
import com.example.api.service.AuthService;
import com.example.api.service.user.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        return userRegistrationService.registerUser(username, password);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        return authService.authenticateUser(username, password);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String bearerToken) {
        return authService.logoutUser(bearerToken);
    }
}
