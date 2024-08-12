package com.writon.admin.domain.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueRequestDto {
  private String accessToken;
  private String refreshToken;
}
