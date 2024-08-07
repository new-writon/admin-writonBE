package com.writon.admin.domain.repository.user;

import com.writon.admin.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}