package com.writon.admin.domain.repository.question;

import com.writon.admin.domain.entity.question.Keyword;
import com.writon.admin.domain.entity.question.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

  List<Question> findByChallengeId(Long challengeId);

  int countByKeyword(Keyword keyword);

}