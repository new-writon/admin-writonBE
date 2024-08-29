package com.writon.admin.domain.dto.response.challenge;

import com.writon.admin.domain.entity.lcoal.ChallengeResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateChallengeResponseDto {

  private List<ChallengeResponse> challengeList;
}
