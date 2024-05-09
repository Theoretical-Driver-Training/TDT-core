package com.example.api.service.token;

import com.example.api.model.token.Token;
import com.example.api.repository.token.TokenRepository;
import com.example.api.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {

    @Autowired
    private TokenRepository repository;

    @Autowired
    private JwtUtils jwtUtils;

    public void storeToken(String username, String token) {
        log.info("Saving a token");
        Token tokenEntity = repository.findByUsername(username)
                .orElseGet(() -> {
                    Token newToken = new Token(token, username);
                    repository.save(newToken);
                    log.info("New token created");
                    return newToken;
                });
        tokenEntity.setToken(token);
        repository.save(tokenEntity);
        log.info("Token {} saved", token);
    }

    public boolean isValidBearerToken(String bearerToken) {
        log.info("Validate bearer token: {}", bearerToken);
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            log.error("Bearer token is invalid");
            return false;
        }
        log.info("Bearer token valid");
        return true;
    }

    public String extractBearerToken(String bearerToken) {
        log.info("Extracting bearer token");
        return bearerToken.substring(7);
    }

    public boolean isTokenIsBlacklist(String token) {
        log.info("Validate token is blacklist: {}", token);
        if (jwtUtils.isTokenInBlacklist(token)) {
            log.error("Token is in list of blacklist");
            return true;
        }
        log.info("Token is not in list of blacklist");
        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        log.info("Retrieving username from JWT token");
        return jwtUtils.getUserNameFromJwtToken(token);
    }
}
