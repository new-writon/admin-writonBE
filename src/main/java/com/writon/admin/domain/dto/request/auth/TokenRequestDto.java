package com.writon.admin.domain.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRequestDto {
  private String accessToken;
  private String refreshToken;
}
