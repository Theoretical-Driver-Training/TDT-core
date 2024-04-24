package com.example.api.controller;

import com.example.api.payload.request.ProfileRequest;
import com.example.api.service.user.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProfileByUser(@RequestHeader("Authorization") String bearerToken, @RequestBody ProfileRequest request) {
        return profileService.updateProfileByUser(bearerToken, request.getFirstName(), request.getLastName(),
                request.getMiddleName(), request.getWeight(), request.getHeight(), request.getBirthDate());
    }

    @GetMapping
    public ResponseEntity<?> getProfileByUserId(@RequestHeader("Authorization") String bearerToken) {
        return profileService.getProfile(bearerToken);
    }



}
