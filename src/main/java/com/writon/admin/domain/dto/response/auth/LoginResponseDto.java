package com.writon.admin.domain.dto.response.auth;

import com.writon.admin.domain.entity.organization.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
  private String email;

  public static LoginResponseDto of(AdminUser adminUser) {
    return new LoginResponseDto(
      adminUser.getIdentifier()
    );
  }
}
