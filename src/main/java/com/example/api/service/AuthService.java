package com.example.api.service;

import com.example.api.model.user.Role;
import com.example.api.model.user.User;
import com.example.api.payload.response.AuthResponse;
import com.example.api.security.jwt.JwtUtils;
import com.example.api.service.token.BlacklistTokenService;
import com.example.api.service.token.TokenService;
import com.example.api.service.user.UserDetailsImpl;
import com.example.api.service.user.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BlacklistTokenService blacklistTokenService;

    public ResponseEntity<?> registerUser(String username, String password) {
        logger.info("Registering user {}", username);
        if (userDetailsService.existsByUsername(username)) {
            logger.error("User {} already exists", username);
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole(Role.USER);
        userDetailsService.saveUser(user);

        logger.info("User {} registered successfully", username);
        return ResponseEntity.ok().body("User registered successfully");
    }

    public ResponseEntity<?> authenticateUser(String username, String password) {
        logger.info("Authenticating user {}", username);
        if (!userDetailsService.existsByUsername(username)) {
            logger.info("User {} does not exist", username);
            return ResponseEntity.badRequest().body("User does not exist");
        }

        Authentication authentication = authenticate(username, password);
        String token = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        tokenService.storeToken(username, userDetails.getUsername());
        logger.info("User {} authenticated successfully", username);
        return ResponseEntity.ok().body(new AuthResponse(token, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    private Authentication authenticate(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    public ResponseEntity<?> logoutUser(String bearerToken) {
        logger.info("Logout user {}", bearerToken);

        blacklistTokenService.addToken(bearerToken);

        logger.info("User {} logged out successfully", bearerToken);
        return ResponseEntity.ok().body("User logged out successfully");
    }
}
