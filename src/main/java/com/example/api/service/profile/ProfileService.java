package com.example.api.service.profile;

import com.example.api.model.user.User;
import com.example.api.payload.response.ProfileResponse;
import com.example.api.security.jwt.JwtUtils;
import com.example.api.service.token.TokenService;
import com.example.api.service.user.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class ProfileService {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TokenService tokenService;

    public ResponseEntity<?> updateProfileByUser(String bearerToken, String firstName, String lastName,
                                                 String middleName, double weight, double height, Date birthDate) {
        log.info("Updating profile by user");
        if (tokenService.isValidBearerToken(bearerToken)) {
            log.error("Bearer token expired {}", bearerToken);
            return ResponseEntity.badRequest().body("Bearer token expired");
        }

        String token = tokenService.extractBearerToken(bearerToken);
        if (jwtUtils.isTokenInBlacklist(token)) {
            log.error("User token is in list of blacklist");
            return ResponseEntity.badRequest().body("User token is in list of blacklist");
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);
        log.info("Update user profile {}", username);
        User user = userDetailsService.getUserEntityByUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setWeight(weight);
        user.setHeight(height);
        user.setBirthDate(birthDate);
        userDetailsService.saveUser(user);
        log.info("Profile updated");
        return ResponseEntity.ok().body("Profile updated");
    }

    public ResponseEntity<?> getProfile(String bearerToken) {
        log.info("Getting profile");
        if (tokenService.isValidBearerToken(bearerToken)) {
            log.error("Bearer token expired {}", bearerToken);
            return ResponseEntity.badRequest().body("Bearer token expired");
        }

        String token = tokenService.extractBearerToken(bearerToken);
        if (jwtUtils.isTokenInBlacklist(token)) {
            log.error("User token is in list of blacklist");
            return ResponseEntity.badRequest().body("User token is in list of blacklist");
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);
        User user = userDetailsService.getUserEntityByUsername(username);
        ProfileResponse profileResponse = new ProfileResponse(user.getLastPasswordChangeDate(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getWeight(), user.getHeight(), user.getBirthDate());

        log.info("Get profile response");
        return ResponseEntity.ok().body(profileResponse);
    }
}
