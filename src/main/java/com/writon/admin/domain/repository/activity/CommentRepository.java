package com.writon.admin.domain.repository.activity;

import com.writon.admin.domain.entity.activity.Comment;
import com.writon.admin.domain.entity.activity.CommentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, CommentId> {

}