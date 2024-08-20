package com.writon.admin.domain.dto.response.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditOrganizationResponseDto {

  private String organizationName;
  private String themeColor;
  private String organizationLogo;

}
