package com.writon.admin.domain.repository.question;

import com.writon.admin.domain.entity.question.Keyword;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

  Optional<Keyword> findByKeyword(String keyword);
}