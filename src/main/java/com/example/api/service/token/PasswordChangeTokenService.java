package com.example.api.service.token;

import com.example.api.model.token.PasswordChangeToken;
import com.example.api.model.user.User;
import com.example.api.repository.token.ChangePasswordTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordChangeTokenService {

    private final Logger logger = LoggerFactory.getLogger(PasswordChangeTokenService.class);

    @Value(value = "${spring.jwt.token.expirationMs.hour}")
    private static int EXPIRATION;

    @Autowired
    private ChangePasswordTokenRepository repository;

    public PasswordChangeToken getOnCreateChangePasswordToken(User user) {
        return repository.findByUser(user)
                .map(this::updateExistingToken)
                .orElseGet(() -> createNewChangePasswordToken(user));
    }

    private PasswordChangeToken createNewChangePasswordToken(User user) {
        logger.info("Creating new password change token");
        PasswordChangeToken changePasswordToken = new PasswordChangeToken();
        changePasswordToken.setUser(user);
        changePasswordToken.setExpiryDate(new Date(System.currentTimeMillis() + EXPIRATION));
        changePasswordToken.setToken(generateUniqueToken());
        logger.info("Created new password change token");
        return repository.save(changePasswordToken);
    }

    private PasswordChangeToken updateExistingToken(PasswordChangeToken token) {
        logger.info("Updating existing password change token");
        token.setToken(generateUniqueToken());
        token.setExpiryDate(new Date(System.currentTimeMillis() + EXPIRATION));
        token = repository.save(token);
        logger.info("Updated existing password change token");
        return token;
    }

    public Optional<PasswordChangeToken> getByToken(String token) {
        logger.info("Retrieving password change token");
        return repository.findByToken(token);
    }

    private String generateUniqueToken() {
        logger.info("Generating unique token");
        while (true) {
            String token = UUID.randomUUID().toString();
            if (!repository.existsByToken(token)) {
                logger.info("Generated unique token");
                return token;
            }
        }
    }

    public String extractChangeToken(String changeToken) {
        logger.info("Extracting change token");
        return changeToken.substring(7);
    }

    public boolean isValidChangeToken(String changeToken) {
        logger.info("Validate change token: {}", changeToken);
        if (changeToken == null || !changeToken.startsWith("Change ")) {
            logger.error("Change token is invalid: {}", changeToken);
            return false;
        }
        logger.info("Change token valid: {}", changeToken);
        return true;
    }
}
