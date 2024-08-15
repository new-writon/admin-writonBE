package com.writon.admin.domain.dto.request.challenge;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChallengeInfoRequestDto {

  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private List<LocalDate> processDates;

}
