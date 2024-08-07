package com.writon.admin.domain.repository.activity;

import com.writon.admin.domain.entity.user.UserChallenge;
import com.writon.admin.domain.entity.user.UserChallengeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, UserChallengeId> {

}