package com.writon.admin.domain.dto.response.auth;

import com.writon.admin.domain.entity.organization.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignUpResponseDto {

  private Long id;
  private String identifier;

}
