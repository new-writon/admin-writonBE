package com.writon.admin.domain.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
  private String identifier;
  private String password;

  public UsernamePasswordAuthenticationToken toAuthentication() {
    return new UsernamePasswordAuthenticationToken(identifier, password);
  }

}
