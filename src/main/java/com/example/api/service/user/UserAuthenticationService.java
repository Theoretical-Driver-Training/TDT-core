package com.example.api.service.user;

import com.example.api.model.user.User;
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

    public User getUserAuthenticated(String bearerToken) {
        String token = validateAndExtractToken(bearerToken);
        if (token == null) return null;

        String username = tokenService.getUserNameFromJwtToken(token);
        if (username == null) {
            log.warn("JWT Token does not contain a valid username");
            return null;
        }

        User user = userDetailsService.getUserEntityByUsername(username);
        if (user == null) {
            log.warn("User {} not found", username);
        }

        return user;
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
}
