package com.example.api.service.user;

import com.example.api.payload.response.AuthResponse;
import com.example.api.security.jwt.JwtUtils;
import com.example.api.service.token.BlacklistTokenService;
import com.example.api.service.token.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserAuthenticationService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BlacklistTokenService blacklistTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<?> authenticateUser(String username, String password) {
        log.info("Authenticating user {}", username);
        if (!userDetailsService.existsUserByUsername(username)) {
            String errorMsg = "User does not exist";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }

        Authentication authentication = authenticate(username, password);
        String token = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        tokenService.storeToken(username, token);

        log.info("User {} authenticated successfully", username);
        return ResponseEntity.ok().body(new AuthResponse(token, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    private Authentication authenticate(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser(String bearerToken) {
        log.info("Logout user {}", bearerToken);

        String token = validateAndExtractToken(bearerToken);
        if (token == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        blacklistTokenService.saveToken(token);
        SecurityContextHolder.clearContext();

        log.info("User {} logged out successfully", bearerToken);
        return ResponseEntity.ok().body("User logged out successfully");
    }

    public ResponseEntity<?> validateUserAuthenticationBearerToken(String bearerToken) {
        String token = validateAndExtractToken(bearerToken);
        if (token == null) {
            String errorMsg = "Bearer token is invalid";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }

        String username = getUsernameFromJwtToken(token);
        UserDetailsImpl userDetails = getUserDetailsFromSecurityContext();

        if (!userDetails.getUsername().equals(username)) {
            String errorMsg = "User is not authenticated";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }

        return ResponseEntity.ok().build();
    }

    private String validateAndExtractToken(String bearerToken) {
        if (bearerToken == null || !tokenService.isValidBearerToken(bearerToken)) {
            log.warn("Invalid or null bearer token");
            return null;
        }

        String token = tokenService.extractBearerToken(bearerToken);
        if (tokenService.isTokenIsBlacklist(token)) {
            log.warn("Token is blacklisted: {}", token);
            return null;
        }

        return token;
    }

    private String getUsernameFromJwtToken(String token) {
        return tokenService.getUserNameFromJwtToken(token);
    }

    public UserDetailsImpl getUserDetailsFromSecurityContext() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
