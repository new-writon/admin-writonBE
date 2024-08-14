package com.writon.admin.domain.dto.request.challenge;

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
  private List<LocalDate> dates;
  private List<String> basicQuestions;
  private List<SpecialQuestionResponse> specialQuestions;
  private List<String> emailList;

}
