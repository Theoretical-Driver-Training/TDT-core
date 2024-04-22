package com.example.api.service.token;

import com.example.api.model.token.BlacklistToken;
import com.example.api.repository.token.BlacklistTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BlacklistTokenService {

    private final Logger logger = LoggerFactory.getLogger(BlacklistTokenService.class);

    private final BlacklistTokenRepository repository;

    public BlacklistTokenService(BlacklistTokenRepository repository) {
        this.repository = repository;
    }

    public void addToken(String token) {
        logger.info("Adding user token to blacklist");
        BlacklistToken blacklistToken = new BlacklistToken();
        blacklistToken.setToken(token);
        blacklistToken.setExpiryDate(new Date(System.currentTimeMillis()));
        logger.info("Adding token: {}", blacklistToken);
        repository.save(blacklistToken);
    }

    public boolean exists(String token) {
        return repository.existsByToken(token);
    }
}
