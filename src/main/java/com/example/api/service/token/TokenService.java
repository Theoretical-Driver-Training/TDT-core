package com.example.api.service.token;

import com.example.api.model.token.Token;
import com.example.api.repository.token.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final TokenRepository repository;

    public TokenService(TokenRepository repository) {
        this.repository = repository;
    }

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
        return bearerToken == null || !bearerToken.startsWith("Bearer ");
    }

    public String extractToken(String bearerToken) {
        return bearerToken.substring(7);
    }
}
