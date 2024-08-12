package com.writon.admin.domain.dto.request.auth;

import com.writon.admin.domain.entity.organization.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
  private String identifier;
  private String password;

  public UsernamePasswordAuthenticationToken toAuthentication() {
    return new UsernamePasswordAuthenticationToken(identifier, password);
  }

}
