package com.writon.admin.domain.repository.small_talk;

import com.writon.admin.domain.entity.small_talk.SmallTalkComment;
import com.writon.admin.domain.entity.small_talk.SmallTalkCommentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmallTalkCommentRepository extends
    JpaRepository<SmallTalkComment, SmallTalkCommentId> {

}