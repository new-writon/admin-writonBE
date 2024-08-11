package com.writon.admin.domain.repository.challenge;

import com.writon.admin.domain.entity.challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

}