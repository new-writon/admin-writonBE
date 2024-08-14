package com.writon.admin.domain.dto.request.organization;

import com.writon.admin.domain.entity.organization.Position;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRequestDto {

  private String name;
  private String themeColor;
  private List<String> positions;
}
