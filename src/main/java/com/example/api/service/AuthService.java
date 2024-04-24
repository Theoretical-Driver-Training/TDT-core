package com.example.api.service;

import com.example.api.model.user.Role;
import com.example.api.payload.response.AuthResponse;
import com.example.api.payload.response.SignupResponse;
import com.example.api.security.jwt.JwtUtils;
import com.example.api.service.mail.MailService;
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
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

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

    @Autowired
    private MailService mailService;

    public ResponseEntity<?> registerUser(String username, String password) {
        logger.info("Registering user {}", username);
        if (userDetailsService.existUserByUsername(username)) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        userDetailsService.createUser(username, password, new Date(), Role.USER);

        SignupResponse signupResponse = new SignupResponse();
        mailService.sendMail(username, signupResponse.getSubject(), signupResponse.getContent());

        logger.info("User {} registered successfully", username);
        return ResponseEntity.ok().body("User registered successfully");
    }

    public ResponseEntity<?> authenticateUser(String username, String password) {
        logger.info("Authenticating user {}", username);
        if (!userDetailsService.existUserByUsername(username)) {
            return ResponseEntity.badRequest().body("User does not exist");
        }

        Authentication authentication = authenticate(username, password);
        String token = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        tokenService.storeToken(username, token);

        logger.info("User {} authenticated successfully", username);
        return ResponseEntity.ok().body(new AuthResponse(token, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    private Authentication authenticate(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    public ResponseEntity<?> logoutUser(String bearerToken) {
        logger.info("Logout user {}", bearerToken);
        if (tokenService.isValidBearerToken(bearerToken)) {
            return ResponseEntity.badRequest().body("Bearer token is invalid");
        }

        String token = tokenService.extractBearerToken(bearerToken);
        if (tokenService.isTokenIsBlacklist(token)) {
            return ResponseEntity.badRequest().body("User token is in list of blacklist");
        }

        blacklistTokenService.saveToken(token);

        logger.info("User {} logged out successfully", bearerToken);
        return ResponseEntity.ok().body("User logged out successfully");
    }
}
