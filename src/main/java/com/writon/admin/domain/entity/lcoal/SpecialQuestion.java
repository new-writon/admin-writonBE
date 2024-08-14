package com.writon.admin.domain.entity.lcoal;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpecialQuestion {

  private String keyword;
  private List<String> questions;

}
