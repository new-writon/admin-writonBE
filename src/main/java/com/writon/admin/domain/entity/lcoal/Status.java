package com.writon.admin.domain.entity.lcoal;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Status {

  private LocalDate date;

  // 참여: 1, 늦참: 0, 미참여: -1
  private int status;

}
