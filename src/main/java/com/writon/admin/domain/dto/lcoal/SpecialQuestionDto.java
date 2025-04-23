package com.writon.admin.domain.dto.lcoal;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpecialQuestionDto {

  private Long keywordId;
  private String keyword;
  private List<String> questions;

}
