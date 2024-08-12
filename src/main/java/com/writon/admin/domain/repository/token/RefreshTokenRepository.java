package com.writon.admin.domain.repository.token;

import com.writon.admin.domain.entity.token.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

  Optional<RefreshToken> findByIdentifier(String identifier);
}
