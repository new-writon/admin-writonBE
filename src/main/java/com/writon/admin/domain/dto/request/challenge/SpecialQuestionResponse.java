package com.writon.admin.domain.dto.request.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpecialQuestionResponse {

  private String keyword;
  private String[] questions;

}
