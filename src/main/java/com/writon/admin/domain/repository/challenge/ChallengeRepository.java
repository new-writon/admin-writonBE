package com.writon.admin.domain.repository.challenge;

import com.writon.admin.domain.entity.challenge.Challenge;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

  Optional<List<Challenge>> findByOrganizationId(Long organizationId);

}