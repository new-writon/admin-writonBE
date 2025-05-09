package com.writon.admin.domain.controller;

import com.writon.admin.domain.dto.request.challenge.ChallengeInfoRequestDto;
import com.writon.admin.domain.dto.request.challenge.CreateChallengeRequestDto;
import com.writon.admin.domain.dto.request.challenge.QuestionsRequestDto;
import com.writon.admin.domain.dto.response.challenge.ChallengeInfoResponseDto;
import com.writon.admin.domain.dto.response.challenge.CreateChallengeResponseDto;
import com.writon.admin.domain.dto.response.challenge.QuestionsResponseDto;
import com.writon.admin.domain.dto.lcoal.UserStatus;
import com.writon.admin.domain.service.ChallengeService;
import com.writon.admin.global.response.SuccessDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @GetMapping("/questions")
  public SuccessDto<QuestionsResponseDto> getQuestions(@RequestParam Long challengeId) {
    QuestionsResponseDto responseDto = challengeService.getQuestions(challengeId);

    return new SuccessDto<>(responseDto);
  }

  @GetMapping("/info")
  public SuccessDto<ChallengeInfoResponseDto> getInfo(@RequestParam Long challengeId) {
    ChallengeInfoResponseDto responseDto = challengeService.getInfo(challengeId);

    return new SuccessDto<>(responseDto);
  }

  @PutMapping("/questions")
  public SuccessDto<QuestionsResponseDto> putQuestions(
      @RequestParam Long challengeId,
      @RequestBody QuestionsRequestDto requestDto
  ) {
    QuestionsResponseDto responseDto = challengeService.putQuestions(challengeId, requestDto);

    return new SuccessDto<>(responseDto);
  }

  @PutMapping("/info")
  public SuccessDto<ChallengeInfoResponseDto> putInfo(
      @RequestParam Long challengeId,
      @RequestBody ChallengeInfoRequestDto requestDto
  ) {
    ChallengeInfoResponseDto responseDto = challengeService.putInfo(challengeId, requestDto);

    return new SuccessDto<>(responseDto);
  }
}
