package com.writon.admin.domain.repository.activity;

import com.writon.admin.domain.entity.activity.Like;
import com.writon.admin.domain.entity.activity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

}