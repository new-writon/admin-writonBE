package com.writon.admin.domain.util;

import com.writon.admin.domain.entity.organization.AdminUser;
import com.writon.admin.domain.entity.organization.Organization;
import com.writon.admin.domain.repository.organization.AdminUserRepository;
import com.writon.admin.domain.repository.organization.OrganizationRepository;
import com.writon.admin.global.error.CustomException;
import com.writon.admin.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenUtil {

  private final AdminUserRepository adminUserRepository;
  private final OrganizationRepository organizationRepository;

  public AdminUser getAdminUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    AdminUser adminUser = adminUserRepository.findByIdentifier(authentication.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return adminUser;
  }

  public Organization getOrganization() {
    AdminUser adminUser = getAdminUser();
    Organization organization = organizationRepository.findByAdminUserId(adminUser.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.ORGANIZATION_NOT_FOUND));

    return organization;
  }
}
