package com.writon.admin.domain.repository.challenge;

import com.writon.admin.domain.entity.challenge.ChallengeDepositDeduction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeDepositDeductionRepository extends
    JpaRepository<ChallengeDepositDeduction, Integer> {

}