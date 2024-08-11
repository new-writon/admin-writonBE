package com.writon.admin.domain.repository.small_talk;

import com.writon.admin.domain.entity.small_talk.SmallTalkComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmallTalkCommentRepository extends JpaRepository<SmallTalkComment, Long> {

}