package com.writon.admin.domain.dto.request.auth;

import com.writon.admin.domain.entity.organization.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {

  private String identifier;
  private String password;

  public AdminUser toAdminUser(PasswordEncoder passwordEncoder) {
    return AdminUser.builder()
        .identifier(identifier)
        .password(passwordEncoder.encode(password))
        .build();
  }
}
