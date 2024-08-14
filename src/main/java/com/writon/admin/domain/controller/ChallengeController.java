package com.writon.admin.domain.controller;

import com.writon.admin.domain.dto.request.challenge.CreateChallengeRequestDto;
import com.writon.admin.domain.dto.response.challenge.CreateChallengeResponseDto;
import com.writon.admin.domain.entity.lcoal.UserStatus;
import com.writon.admin.domain.service.ChallengeService;
import com.writon.admin.global.response.SuccessDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {

  private final ChallengeService challengeService;

  @PostMapping
  public SuccessDto<CreateChallengeResponseDto> createChallenge(
      @RequestBody CreateChallengeRequestDto createChallengeRequestDto
  ) {
    CreateChallengeResponseDto responseDto = challengeService.createChallenge(
        createChallengeRequestDto);

    return new SuccessDto<>(responseDto);
  }

  @GetMapping("/dashboard")
  public SuccessDto<List<UserStatus>> getDashboard(@RequestParam Long challengeId) {
    List<UserStatus> userStatusList = challengeService.getDashboard(challengeId);

    return new SuccessDto<>(userStatusList);
  }

}
