package com.writon.admin.domain.repository.organization;

import com.writon.admin.domain.entity.organization.AdminUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

  Optional<AdminUser> findByIdentifier(String identifier);
  boolean existsByIdentifier(String identifier);

}