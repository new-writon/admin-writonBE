package com.writon.admin.domain.service;

import com.writon.admin.domain.dto.request.challenge.ChallengeInfoRequestDto;
import com.writon.admin.domain.dto.request.challenge.CreateChallengeRequestDto;
import com.writon.admin.domain.dto.request.challenge.QuestionsRequestDto;
import com.writon.admin.domain.dto.response.challenge.ChallengeInfoResponseDto;
import com.writon.admin.domain.dto.response.challenge.QuestionsResponseDto;
import com.writon.admin.domain.entity.lcoal.SpecialQuestion;
import com.writon.admin.domain.entity.lcoal.ChallengeResponse;
import com.writon.admin.domain.dto.response.challenge.CreateChallengeResponseDto;
import com.writon.admin.domain.entity.lcoal.Status;
import com.writon.admin.domain.entity.lcoal.UserStatus;
import com.writon.admin.domain.entity.activity.UserTemplate;
import com.writon.admin.domain.entity.challenge.Challenge;
import com.writon.admin.domain.entity.challenge.ChallengeDay;
import com.writon.admin.domain.entity.challenge.Email;
import com.writon.admin.domain.entity.organization.Organization;
import com.writon.admin.domain.entity.question.Keyword;
import com.writon.admin.domain.entity.question.Question;
import com.writon.admin.domain.entity.user.UserChallenge;
import com.writon.admin.domain.repository.activity.UserTemplateRepository;
import com.writon.admin.domain.repository.challenge.ChallengeDayRepository;
import com.writon.admin.domain.repository.challenge.ChallengeRepository;
import com.writon.admin.domain.repository.challenge.EmailRepository;
import com.writon.admin.domain.repository.question.KeywordRepository;
import com.writon.admin.domain.repository.question.QuestionRepository;
import com.writon.admin.domain.repository.user.UserChallengeRepository;
import com.writon.admin.domain.util.TokenUtil;
import com.writon.admin.global.error.CustomException;
import com.writon.admin.global.error.ErrorCode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
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
  private final UserChallengeRepository userChallengeRepository;
  private final UserTemplateRepository userTemplateRepository;
  private final EmailService emailService;

  // ========== Create API ==========
  public CreateChallengeResponseDto createChallenge(CreateChallengeRequestDto requestDto) {
    // 1. 조직정보 추출
    Organization organization = tokenUtil.getOrganization();

    // 2. 챌린지 정보 저장
    Challenge challenge = challengeRepository.save(new Challenge(
        requestDto.getName(),
        requestDto.getStartDate(),
        requestDto.getEndDate(),
        organization,
        0L
    ));

    // 3. 챌린지 날짜정보 저장
    for (LocalDate date : requestDto.getProcessDates()) {
      challengeDayRepository.save(new ChallengeDay(date, challenge));
    }

    // 4. 질문정보 저장
    for (String basicQuestion : requestDto.getBasicQuestions()) {
      questionRepository.save(
          new Question(basicQuestion, "베이직 질문", challenge)
      );
    }
    for (SpecialQuestion specialQuestionResponse : requestDto.getSpecialQuestions()) {
      Keyword keyword = keywordRepository.save(new Keyword(specialQuestionResponse.getKeyword()));

      for (String specialQuestion : specialQuestionResponse.getQuestions()) {
        questionRepository.save(
            new Question(specialQuestion, "스페셜 질문", challenge, keyword)
        );
      }
    }

    // 5. 이메일 전송 & 정보 저장
    for (String email : requestDto.getEmailList()) {
      emailService.sendEmail(challenge, email);
      emailRepository.save(new Email(email, challenge));
    }

    // 6. Response 생성
    List<Challenge> challenges = challengeRepository.findByOrganizationId(organization.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));
    List<ChallengeResponse> challengeList = challenges.stream()
        .map(entity -> new ChallengeResponse(entity.getId(), entity.getName()))
        .collect(Collectors.toList());

    return new CreateChallengeResponseDto(challengeList);
  }

  // ========== Get Dashboard API ==========
  public List<UserStatus> getDashboard(Long challengeId) {
    // 1. 챌린지 날짜 리스트 추출
    List<ChallengeDay> challengeDayList = challengeDayRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    challengeDayList = challengeDayList.stream()
        .sorted(Comparator.comparing(ChallengeDay::getDay))
        .toList();

    // 2. 챌린지 참여 유저 리스트 추출
    List<UserChallenge> userChallengeList = userChallengeRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    // 3. 유저별 참여여부 확인
    List<UserStatus> statusTable = new ArrayList<>();

    for (UserChallenge userChallenge : userChallengeList) {
      List<Status> statusList = new ArrayList<>();
      List<UserTemplate> userTemplateList = userTemplateRepository.findByUserChallengeId(
              userChallenge.getId())
          .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

      for (ChallengeDay challengeDay : challengeDayList) {
        // 참여여부 확인과정
        int status = -1;
        for (UserTemplate userTemplate : userTemplateList) {
          if (userTemplate.getTemplateDate().equals(challengeDay.getDay())) {
            status = userTemplate.getComplete() ? 1 : 0;
            break;
          }
        }
        statusList.add(new Status(challengeDay.getDay(), status));
      }
      statusTable.add(new UserStatus(userChallenge.getAffiliation().getNickname(), statusList));
    }

    return statusTable;
  }

  // ========== Get Questions API ==========
  public QuestionsResponseDto getQuestions(Long challengeId) {
    // 1. 질문 리스트 추출
    List<Question> questionList = questionRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException((ErrorCode.ETC_ERROR)));
    questionList.sort(Comparator.comparing(question ->
        question.getKeyword() != null
            ? question.getKeyword().getId()
            : Long.MAX_VALUE)); // keyword 기준으로 정렬

    List<String> basicQuestions = questionList.stream()
        .filter(question -> "베이직 질문".equals(question.getCategory()))
        .map(Question::getQuestion)
        .toList();

    List<SpecialQuestion> specialQuestions = questionList.stream()
        .filter(question -> question.getKeyword() != null)
        .collect(Collectors.groupingBy(
            question -> question.getKeyword().getKeyword(),
            LinkedHashMap::new,  // 순서를 유지하기 위해 LinkedHashMap 사용
            Collectors.mapping(Question::getQuestion, Collectors.toList())
        ))
        .entrySet().stream()
        .map(entry -> new SpecialQuestion(entry.getKey(), entry.getValue()))
        .toList();

    return new QuestionsResponseDto(basicQuestions, specialQuestions);
  }

  // ========== Get Info API ==========
  public ChallengeInfoResponseDto getInfo(Long challengeId) {
    // 1. 챌린지 기본 정보 가져오기
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    // 2. 챌린지 날짜 정보 가져오기
    List<ChallengeDay> challengeDays = challengeDayRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    return new ChallengeInfoResponseDto(
        challenge.getName(),
        challenge.getStartAt(),
        challenge.getFinishAt(),
        challengeDays.stream().map(ChallengeDay::getDay).collect(Collectors.toList())
    );
  }

  // ========== Put Questions API ==========
  public QuestionsResponseDto putQuestions(Long challengeId, QuestionsRequestDto requestDto) {
    // 1. 챌린지 조회
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    // 2. 질문 리스트 조회
    List<Question> questionList = questionRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException((ErrorCode.ETC_ERROR)));

    // 3. 기존 BasicQuestion 중 입력값에 없는 질문들을 삭제
    List<Question> basicQuestionsToDelete = questionList.stream()
        .filter(question -> "베이직 질문".equals(question.getCategory()) &&
            !requestDto.getBasicQuestions().contains(question.getQuestion()))
        .toList();

    questionRepository.deleteAll(basicQuestionsToDelete);

    // 4. 새로 추가된 BasicQuestion 추가
    for (String basicQuestion : requestDto.getBasicQuestions()) {
      boolean exists = questionList.stream()
          .anyMatch(question -> "베이직 질문".equals(question.getCategory()) &&
              question.getQuestion().equals(basicQuestion));
      if (!exists) {
        questionRepository.save(
            new Question(basicQuestion, "베이직 질문", challenge)
        );
      }
    }

    // 5. specialQuestion 처리
    for (SpecialQuestion specialQuestion : requestDto.getSpecialQuestions()) {
      String keyword = specialQuestion.getKeyword();
      List<String> questions = specialQuestion.getQuestions();

      // 기존 질문 리스트에서 현재 specialQuestion의 키워드에 해당하는 질문들을 필터링
      List<Question> existingQuestions = questionList.stream()
          .filter(question -> question.getKeyword() != null &&
              keyword.equals(question.getKeyword().getKeyword()))
          .toList();

      // 기존 질문 리스트 중에서 specialQuestion에 없는 질문들은 삭제
      List<Question> specialQuestionsToDelete = existingQuestions.stream()
          .filter(question -> !questions.contains(question.getQuestion()))
          .toList();
      questionRepository.deleteAll(specialQuestionsToDelete);

      // 새로 추가할 질문들 중 기존에 없는 질문들을 추가
      for (String questionText : questions) {
        boolean exists = existingQuestions.stream()
            .anyMatch(question -> question.getQuestion().equals(questionText));
        if (!exists) {
          // 키워드가 없으면 추가
          Keyword keywordEntity = keywordRepository.findByKeyword(keyword)
              .orElseGet(() -> {
                Keyword newKeyword = new Keyword(keyword);
                keywordRepository.save(newKeyword);
                return newKeyword;
              });

          questionRepository.save(
              new Question(questionText, "스페셜 질문", challenge, keywordEntity)
          );
        }
      }
    }

    // 6. questionList에서 specialQuestion에 없는 키워드의 질문들을 삭제
    List<Question> specialQuestionsToDelete = questionList.stream()
        .filter(question -> question.getKeyword() != null &&
            requestDto.getSpecialQuestions().stream()
                .noneMatch(specialQuestion -> specialQuestion.getKeyword()
                    .equals(question.getKeyword().getKeyword())))
        .toList();
    questionRepository.deleteAll(specialQuestionsToDelete);

    // 7. questionList에서 삭제된 질문들의 키워드를 제거
    specialQuestionsToDelete.stream()
        .map(Question::getKeyword)
        .distinct()
        .forEach(keyword -> {
          if (questionRepository.countByKeyword(keyword) == 0) {
            keywordRepository.delete(keyword);
          }
        });

    // 8. 수정된 질문 리스트 다시 조회
    List<Question> updatedQuestionList = questionRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    // 9. 기본 질문과 스페셜 질문 분리 및 응답 생성
    List<String> basicQuestions = updatedQuestionList.stream()
        .filter(question -> "베이직 질문".equals(question.getCategory()))
        .map(Question::getQuestion)
        .toList();

    List<SpecialQuestion> specialQuestions = updatedQuestionList.stream()
        .filter(question -> question.getKeyword() != null)
        .collect(Collectors.groupingBy(
            question -> question.getKeyword().getKeyword(),
            LinkedHashMap::new,  // 순서를 유지하기 위해 LinkedHashMap 사용
            Collectors.mapping(Question::getQuestion, Collectors.toList())
        ))
        .entrySet().stream()
        .map(entry -> new SpecialQuestion(entry.getKey(), entry.getValue()))
        .toList();

    // 10. QuestionsResponseDto 생성 후 반환
    return new QuestionsResponseDto(basicQuestions, specialQuestions);
  }

  // ========== Put Info API ==========
  public ChallengeInfoResponseDto putInfo(Long challengeId, ChallengeInfoRequestDto requestDto) {
    // 1. 챌린지 기본 정보 조회
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    // 2. 챌린지 기본 정보 수정 및 저장
    challenge.setName(requestDto.getName());
    challenge.setStartAt(requestDto.getStartDate());
    challenge.setFinishAt(requestDto.getEndDate());
    Challenge editedChallenge = challengeRepository.save(challenge);

    // 3. 챌린지 날짜 정보 조회
    List<ChallengeDay> challengeDays = challengeDayRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    // 4. 챌린지 날짜 정보 수정 및 저장
    // 1) 새로운 날짜가 기존 리스트에 없으면 추가
    for (LocalDate newDate : requestDto.getProcessDates()) {
      boolean exists = challengeDays.stream()
          .anyMatch(day -> day.getDay().equals(newDate));
      if (!exists) {
        ChallengeDay newDay = challengeDayRepository.save(new ChallengeDay(
            newDate,
            editedChallenge
        ));
      }
    }

    // 2) 기존 날짜가 새로운 리스트에 없으면 삭제
    for (ChallengeDay existingDay : challengeDays) {
      boolean stillExists = requestDto.getProcessDates().stream()
          .anyMatch(date -> date.equals(existingDay.getDay()));
      if (!stillExists) {
        challengeDayRepository.delete(existingDay);
      }
    }

    // 5. 변경된 날짜 정보 조회
    List<ChallengeDay> editedChallengeDays = challengeDayRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    return new ChallengeInfoResponseDto(
        editedChallenge.getName(),
        editedChallenge.getStartAt(),
        editedChallenge.getFinishAt(),
        editedChallengeDays.stream().map(ChallengeDay::getDay).collect(Collectors.toList())
    );
  }
}
