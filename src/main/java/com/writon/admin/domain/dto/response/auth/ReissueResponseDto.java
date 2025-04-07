package com.writon.admin.domain.dto.response.auth;

import com.writon.admin.global.config.auth.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueResponseDto {
  private TokenDto tokenDto;
}
