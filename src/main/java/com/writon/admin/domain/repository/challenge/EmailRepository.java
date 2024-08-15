package com.writon.admin.domain.repository.challenge;

import com.writon.admin.domain.entity.challenge.Email;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {

  Optional<List<Email>> findByChallengeId(Long challengeId);

}