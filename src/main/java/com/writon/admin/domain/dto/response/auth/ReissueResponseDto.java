package com.writon.admin.domain.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueResponseDto {

  private String accessToken;
  private String refreshToken;
}
