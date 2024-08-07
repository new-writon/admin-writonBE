package com.writon.admin.domain.repository.question;

import com.writon.admin.domain.entity.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

}