package com.example.api.repository.token;

import com.example.api.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUsername(String username);

}
