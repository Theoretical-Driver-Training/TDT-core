package com.example.api.service.token;

import com.example.api.model.token.PasswordChangeToken;
import com.example.api.model.user.User;
import com.example.api.repository.token.ChangePasswordTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordChangeTokenService {

    @Value("${spring.jwt.token.expirationMs.hour}")
    private static int EXPIRATION;

    private final ChangePasswordTokenRepository repository;

    public PasswordChangeTokenService(ChangePasswordTokenRepository changePasswordTokenRepository) {
        this.repository = changePasswordTokenRepository;
    }

    public PasswordChangeToken getOnCreateChangePasswordToken(User user) {
        return repository.findByUser(user)
                .map(this::updateExistingToken)
                .orElseGet(() -> createNewChangePasswordToken(user));
    }

    public Optional<PasswordChangeToken> getByToken(String token) {
        return repository.findByToken(token);
    }

    boolean existsByToken(String token) {
        return repository.existsByToken(token);
    }

    private PasswordChangeToken createNewChangePasswordToken(User user) {
        PasswordChangeToken changePasswordToken = new PasswordChangeToken();
        changePasswordToken.setUser(user);
        changePasswordToken.setExpiryDate(new Date(System.currentTimeMillis() + EXPIRATION));
        changePasswordToken.setToken(generateUniqueToken());
        return repository.save(changePasswordToken);
    }

    private PasswordChangeToken updateExistingToken(PasswordChangeToken token) {
        token.setToken(generateUniqueToken());
        token.setExpiryDate(new Date(System.currentTimeMillis() + EXPIRATION));
        token = repository.save(token);
        return token;
    }

    private String generateUniqueToken() {
        while (true) {
            String token = UUID.randomUUID().toString();
            if (!repository.existsByToken(token)) {
                return token;
            }
        }
    }

    public boolean isValidChangePasswordToken(String changeToken) {
        return changeToken == null || !changeToken.startsWith("Change ");
    }

    public String extractToken(String changeToken) {
        return changeToken.substring(7);
    }
}
