package com.example.api.service.token;

import com.example.api.model.token.Token;
import com.example.api.model.token.TokenBlacklist;
import com.example.api.repository.token.TokenBlacklistRepository;
import com.example.api.repository.token.TokenRepository;
import com.example.api.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenBlacklistRepository blacklistRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Value(value = "${spring.jwt.token.expirationMs.day}")
    private long EXPIRATION;

    public void storeToken(String username, String token) {
        log.info("Attempting to store or update a token for username: {}", username);
        Optional<Token> existingToken = tokenRepository.findByUsername(username);

        if (existingToken.isPresent()) {
            Token tokenEntity = existingToken.get();
            if (!jwtUtils.validateJwtToken(tokenEntity.getToken()) || tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
                log.info("Token is invalid or expired, updating token for username: {}", username);
                updateToken(tokenEntity, token);
            } else {
                log.info("Existing token is still valid and no update needed for username: {}", username);
            }
        } else {
            log.info("No existing token found, creating new token for username: {}", username);
            Token newToken = createNewToken(username, token);
            tokenRepository.save(newToken);
            log.info("New token created for username: {}", username);
        }
    }

    private Token createNewToken(String username, String token) {
        LocalDateTime expiryDate = LocalDateTime.ofInstant(Instant.now().plusMillis(EXPIRATION), ZoneId.systemDefault());
        return new Token(username, token, expiryDate);
    }

    private void updateToken(Token tokenEntity, String newToken) {
        LocalDateTime newExpiryDate = LocalDateTime.ofInstant(Instant.now().plusMillis(EXPIRATION), ZoneId.systemDefault());
        tokenEntity.setToken(newToken);
        tokenEntity.setExpiryDate(newExpiryDate);
        tokenRepository.save(tokenEntity);
        log.info("Token for username: {} has been updated", tokenEntity.getUsername());
    }

    public boolean isTokenInBlacklist(String token) {
        log.info("Checking if token is in blacklist: {}", token);
        return blacklistRepository.existsByToken(token);
    }

    @Transactional
    public void addTokenToBlacklist(String token) {
        log.info("Adding token to blacklist: {}", token);
        blacklistRepository.save(new TokenBlacklist(token, new Date(System.currentTimeMillis())));
        log.info("Token added to blacklist.");
    }

    public String validateAndExtractToken(String bearerToken) {
        log.info("Validating token");
        if (!isValidToken(bearerToken)) {
            log.error("Invalid token");
            return null;
        }

        String token = extractBearerToken(bearerToken);
        if (isTokenInBlacklist(token)) {
            log.info("Token {} already exists in blacklist", token);
            return null;
        }

        return token;
    }

    private boolean isValidToken(String bearerToken) {
        log.debug("Checking if token is valid");
        return bearerToken != null && bearerToken.startsWith("Bearer ");
    }

    private String extractBearerToken(String token) {
        log.debug("Extracting token");
        return token.substring(7);
    }
}
