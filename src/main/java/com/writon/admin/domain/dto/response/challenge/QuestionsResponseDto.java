package com.writon.admin.domain.dto.response.challenge;

import com.writon.admin.domain.dto.lcoal.SpecialQuestionDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionsResponseDto {

  private List<String> basicQuestions;
  private List<SpecialQuestion> specialQuestions;

}
