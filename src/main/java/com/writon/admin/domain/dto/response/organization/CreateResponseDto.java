package com.writon.admin.domain.dto.response.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateResponseDto {

  private Long organizationId;
  private String organizationName;
  private String organizationLogo; // nullable

}
