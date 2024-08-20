package com.writon.admin.domain.dto.request.challenge;

import com.writon.admin.domain.entity.lcoal.SpecialQuestion;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateChallengeRequestDto {

  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private List<LocalDate> processDates;
  private List<String> basicQuestions;
  private List<SpecialQuestion> specialQuestions;
  private List<String> emailList;

}
