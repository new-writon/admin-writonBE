package com.writon.admin.domain.repository.organization;

import com.writon.admin.domain.entity.organization.Organization;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
  Optional<Organization> findByAdminUserId(Long adminUserId);
}