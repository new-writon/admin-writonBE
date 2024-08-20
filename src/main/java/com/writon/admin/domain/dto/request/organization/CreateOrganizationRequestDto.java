package com.writon.admin.domain.dto.request.organization;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrganizationRequestDto {

  private String name;
  private String themeColor;
  private List<String> positions;
}
