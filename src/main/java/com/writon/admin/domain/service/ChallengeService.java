package com.writon.admin.domain.service;

import com.writon.admin.domain.dto.request.challenge.CreateChallengeRequestDto;
import com.writon.admin.domain.dto.request.challenge.SpecialQuestionResponse;
import com.writon.admin.domain.dto.response.challenge.ChallengeResponse;
import com.writon.admin.domain.dto.response.challenge.CreateChallengeResponseDto;
import com.writon.admin.domain.entity.challenge.Challenge;
import com.writon.admin.domain.entity.challenge.ChallengeDay;
import com.writon.admin.domain.entity.challenge.Email;
import com.writon.admin.domain.entity.organization.Organization;
import com.writon.admin.domain.entity.question.Keyword;
import com.writon.admin.domain.entity.question.Question;
import com.writon.admin.domain.repository.challenge.ChallengeDayRepository;
import com.writon.admin.domain.repository.challenge.ChallengeRepository;
import com.writon.admin.domain.repository.challenge.EmailRepository;
import com.writon.admin.domain.repository.question.KeywordRepository;
import com.writon.admin.domain.repository.question.QuestionRepository;
import com.writon.admin.domain.util.TokenUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

  private final ChallengeRepository challengeRepository;
  private final ChallengeDayRepository challengeDayRepository;
  private final EmailRepository emailRepository;
  private final QuestionRepository questionRepository;
  private final KeywordRepository keywordRepository;
  private final TokenUtil tokenUtil;

  public CreateChallengeResponseDto createChallenge(CreateChallengeRequestDto requestDto) {
    // 1. 조직정보 추출
    Organization organization = tokenUtil.getOrganization();
    System.out.println("1. " + organization.getName());

    // 2. 챌린지 정보 저장
    Challenge challenge = challengeRepository.save(new Challenge(
        requestDto.getName(),
        requestDto.getStartDate(),
        requestDto.getEndDate(),
        organization
    ));
    System.out.println("2. " + challenge.getName());

    // 3. 챌린지 날짜정보 저장
    for (LocalDate date : requestDto.getDates()) {
      challengeDayRepository.save(new ChallengeDay(date, challenge));
    }
    System.out.println("3. 날짜정보 저장");

    // 4. 질문정보 저장
    for (String basicQuestion : requestDto.getBasicQuestions()) {
      questionRepository.save(
          new Question(basicQuestion, "베이직 질문", challenge)
      );
    }
    for (SpecialQuestionResponse specialQuestionResponse : requestDto.getSpecialQuestions()) {
      Keyword keyword = keywordRepository.save(new Keyword(specialQuestionResponse.getKeyword()));

      for (String specialQuestion : specialQuestionResponse.getQuestions()) {
        questionRepository.save(
            new Question(specialQuestion, "스페셜 질문", challenge, keyword)
        );
      }
    }
    System.out.println("4. 질문정보 저장");

    // 5. 이메일정보 저장
    for (String email : requestDto.getEmailList()) {
      emailRepository.save(new Email(email, challenge));
    }
    System.out.println("5. 이메일정보 저장");

    // 6. Response 생성
    List<Challenge> challenges = challengeRepository.findByOrganizationId(organization.getId());
    List<ChallengeResponse> challengeList = challenges.stream()
        .map(entity -> new ChallengeResponse(entity.getId(), entity.getName()))
        .collect(Collectors.toList());

    return new CreateChallengeResponseDto(challengeList);
  }
}
