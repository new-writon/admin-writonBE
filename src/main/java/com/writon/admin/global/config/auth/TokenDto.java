package com.writon.admin.global.config.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenDto {
  private String accessToken;
  private String refreshToken;
}
