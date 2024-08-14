package com.writon.admin.domain.repository.activity;

import com.writon.admin.domain.entity.activity.UserTemplate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTemplateRepository extends JpaRepository<UserTemplate, Long> {

  Optional<List<UserTemplate>> findByUserChallengeId(Long userChallengeId);

}