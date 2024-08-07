package com.writon.admin.domain.repository.question;

import com.writon.admin.domain.entity.question.QuestionContent;
import com.writon.admin.domain.entity.question.QuestionContentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionContentRepository extends
    JpaRepository<QuestionContent, QuestionContentId> {

}