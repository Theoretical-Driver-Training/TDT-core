package com.example.api.repository.token;

import com.example.api.model.token.TokenChangePassword;
import com.example.api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChangePasswordTokenRepository extends JpaRepository<TokenChangePassword, Long> {

    Optional<TokenChangePassword> findByToken(String token);

    Optional<TokenChangePassword> findByUser(User user);

    boolean existsByToken(String token);

}
