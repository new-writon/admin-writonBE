package com.writon.admin.domain.repository.token;

import com.writon.admin.domain.entity.token.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

  Optional<RefreshToken> findByIdentifier(String identifier);

  default Optional<RefreshToken> deleteByIdentifier(String identifier) {
    Optional<RefreshToken> refreshToken = findByIdentifier(identifier);
    refreshToken.ifPresent(this::delete);

    return refreshToken;
  }

}
