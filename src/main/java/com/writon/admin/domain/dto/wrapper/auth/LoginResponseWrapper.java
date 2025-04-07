package com.writon.admin.domain.dto.wrapper.auth;

import com.writon.admin.domain.dto.response.auth.LoginResponseDto;
import com.writon.admin.global.config.auth.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseWrapper {
  private TokenDto tokenDto;
  private LoginResponseDto loginResponseDto;
}
