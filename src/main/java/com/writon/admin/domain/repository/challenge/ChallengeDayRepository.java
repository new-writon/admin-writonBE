package com.writon.admin.domain.repository.challenge;

import com.writon.admin.domain.entity.challenge.ChallengeDay;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeDayRepository extends JpaRepository<ChallengeDay, Long> {

  List<ChallengeDay> findByChallengeId(Long challengeId);

}