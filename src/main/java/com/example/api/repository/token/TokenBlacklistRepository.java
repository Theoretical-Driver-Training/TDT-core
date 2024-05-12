package com.example.api.repository.token;

import com.example.api.model.token.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

    Boolean existsByToken(String token);

}
