package com.writon.admin.domain.dto.request.challenge;

import com.writon.admin.domain.dto.lcoal.SpecialQuestionDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionsRequestDto {

  private List<String> basicQuestions;
  private List<SpecialQuestion> specialQuestions;

}
