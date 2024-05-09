package com.example.api.service;

import com.example.api.payload.response.AuthResponse;
import com.example.api.security.jwt.JwtUtils;
import com.example.api.service.token.BlacklistTokenService;
import com.example.api.service.token.TokenService;
import com.example.api.service.user.UserDetailsImpl;
import com.example.api.service.user.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

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
            return ResponseEntity.badRequest().body("User does not exist");
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

    public ResponseEntity<?> logoutUser(String bearerToken) {
        log.info("Logout user {}", bearerToken);
        if (tokenService.isValidBearerToken(bearerToken)) {
            return ResponseEntity.badRequest().body("Bearer token is invalid");
        }

        String token = tokenService.extractBearerToken(bearerToken);
        if (tokenService.isTokenIsBlacklist(token)) {
            return ResponseEntity.badRequest().body("User token is in list of blacklist");
        }

        blacklistTokenService.saveToken(token);

        log.info("User {} logged out successfully", bearerToken);
        return ResponseEntity.ok().body("User logged out successfully");
    }
}
