package com.writon.admin.domain.repository.organization;

import com.writon.admin.domain.entity.organization.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Integer> {

}