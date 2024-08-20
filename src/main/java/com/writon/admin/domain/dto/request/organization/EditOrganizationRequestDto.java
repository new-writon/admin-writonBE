package com.writon.admin.domain.dto.request.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditOrganizationRequestDto {

  private String name;
  private String themeColor;
  private String logo;

}
