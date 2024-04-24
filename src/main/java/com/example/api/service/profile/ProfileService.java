package com.example.api.service.profile;

import com.example.api.model.user.User;
import com.example.api.payload.response.ProfileResponse;
import com.example.api.security.jwt.JwtUtils;
import com.example.api.service.token.TokenService;
import com.example.api.service.user.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProfileService {

    private final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtils jwtUtils;

    private final TokenService tokenService;

    public ProfileService(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils, TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
    }

    public ResponseEntity<?> updateProfileByUser(String bearerToken, String firstName, String lastName,
                                                 String middleName, double weight, double height, Date birthDate) {
        logger.info("Updating profile by user");
        if (tokenService.isValidBearerToken(bearerToken)) {
            logger.error("Bearer token expired {}", bearerToken);
            return ResponseEntity.badRequest().body("Bearer token expired");
        }

        String token = tokenService.extractBearerToken(bearerToken);
        if (jwtUtils.isTokenInBlacklist(token)) {
            logger.error("User token is in list of blacklist");
            return ResponseEntity.badRequest().body("User token is in list of blacklist");
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> userOptional = userDetailsService.getUserByUsername(username);
        if (userOptional.isEmpty()) {
            logger.error("User {} not found", username);
            return ResponseEntity.badRequest().body("User not found");
        }

        logger.info("Update user profile {}", username);
        User user = userOptional.get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setWeight(weight);
        user.setHeight(height);
        user.setBirthDate(birthDate);
        userDetailsService.saveUser(user);
        logger.info("Profile updated");
        return ResponseEntity.ok().body("Profile updated");
    }

    public ResponseEntity<?> getProfile(String bearerToken) {
        logger.info("Getting profile");
        if (tokenService.isValidBearerToken(bearerToken)) {
            logger.error("Bearer token expired {}", bearerToken);
            return ResponseEntity.badRequest().body("Bearer token expired");
        }

        String token = tokenService.extractBearerToken(bearerToken);
        if (jwtUtils.isTokenInBlacklist(token)) {
            logger.error("User token is in list of blacklist");
            return ResponseEntity.badRequest().body("User token is in list of blacklist");
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> userOptional = userDetailsService.getUserByUsername(username);
        if (userOptional.isEmpty()) {
            logger.error("User {} not found", username);
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();
        ProfileResponse profileResponse = new ProfileResponse(user.getLastPasswordChangeDate(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getWeight(), user.getHeight(), user.getBirthDate());

        logger.info("Get profile response");
        return ResponseEntity.ok().body(profileResponse);
    }

}
