package com.example.api.repository.token;

import com.example.api.model.token.PasswordChangeToken;
import com.example.api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChangePasswordTokenRepository extends JpaRepository<PasswordChangeToken, Long> {

    Optional<PasswordChangeToken> findByToken(String token);

    Optional<PasswordChangeToken> findByUser(User user);

    boolean existsByToken(String token);

}
