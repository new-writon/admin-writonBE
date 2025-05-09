package com.writon.admin.domain.repository.user;

import com.writon.admin.domain.entity.user.UserChallenge;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

  List<UserChallenge> findByChallengeId(Long challengeId);
  List<UserChallenge> findByAffiliationId(Long affiliationId);

}