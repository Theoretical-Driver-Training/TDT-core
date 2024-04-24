package com.example.api.service.token;

import com.example.api.model.token.Token;
import com.example.api.repository.token.TokenRepository;
import com.example.api.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private TokenRepository repository;

    @Autowired
    private JwtUtils jwtUtils;

    public void storeToken(String username, String token) {
        logger.info("Saving a token");
        Token tokenEntity = repository.findByUsername(username)
                .orElseGet(() -> {
                    Token newToken = new Token(token, username);
                    repository.save(newToken);
                    logger.info("New token created");
                    return newToken;
                });
        tokenEntity.setToken(token);
        repository.save(tokenEntity);
        logger.info("Token {} saved", token);
    }

    public boolean isValidBearerToken(String bearerToken) {
        logger.info("Validate bearer token: {}", bearerToken);
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            logger.error("Bearer token is invalid");
            return false;
        }
        logger.info("Bearer token valid");
        return true;
    }

    public String extractBearerToken(String bearerToken) {
        logger.info("Extracting bearer token");
        return bearerToken.substring(7);
    }

    public boolean isTokenIsBlacklist(String token) {
        logger.info("Validate token is blacklist: {}", token);
        if (jwtUtils.isTokenInBlacklist(token)) {
            logger.error("Token is in list of blacklist");
            return true;
        }
        logger.info("Token is not in list of blacklist");
        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        logger.info("Retrieving username from JWT token");
        return jwtUtils.getUserNameFromJwtToken(token);
    }
}
