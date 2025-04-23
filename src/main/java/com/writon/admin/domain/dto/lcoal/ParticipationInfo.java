package com.writon.admin.domain.dto.lcoal;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipationInfo {

  private Long id;
  private Boolean withdrawn;
  private String nickname;
  private int challengeCnt;
  private String challenges;
  private LocalDate startDate;
  private String position;
  private String teamName;
  private LocalDate joinDate;
  private String bank;
  private String accountNum;
  private String email;
  private Long deposit;
  private int writingCnt;
  private int commentCnt;
  private int smallTalkCnt;
  private String oneLine;

}
