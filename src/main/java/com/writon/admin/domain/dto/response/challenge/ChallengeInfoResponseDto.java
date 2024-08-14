package com.writon.admin.domain.dto.response.challenge;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChallengeInfoResponseDto {

  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private List<LocalDate> processDates;

}
