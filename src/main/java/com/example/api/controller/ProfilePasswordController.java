package com.example.api.controller;

import com.example.api.payload.request.PasswordChangeRequest;
import com.example.api.payload.request.PasswordForgotRequest;
import com.example.api.service.ProfilePasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile/password")
public class ProfilePasswordController {

    private final ProfilePasswordService service;

    public ProfilePasswordController(ProfilePasswordService service) {
        this.service = service;
    }

    @PostMapping("/reset")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> resetPassword(@RequestHeader("Authorization") String bearerToken) {
        return service.resetPassword(bearerToken);
    }

    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String passwordChangeToken, @RequestBody PasswordChangeRequest request) {
        return service.changePassword(passwordChangeToken, request.getOldPassword(), request.getNewPassword());
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordForgotRequest request) {
        return service.forgotPassword(request.getUsername());
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validatePassword(@RequestHeader("Authorization") String passwordChangeToken, @RequestBody PasswordChangeRequest request) {
        return service.validateCurrentPassword(passwordChangeToken, request.getOldPassword(), request.getNewPassword());
    }

}
