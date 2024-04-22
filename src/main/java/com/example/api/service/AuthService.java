package com.example.api.service;

import com.example.api.model.user.Role;
import com.example.api.model.user.User;
import com.example.api.service.user.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder encoder;

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
}
