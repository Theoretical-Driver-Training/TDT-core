package com.example.api.repository.token;

import com.example.api.model.token.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {

    Boolean existsByToken(String token);

}
