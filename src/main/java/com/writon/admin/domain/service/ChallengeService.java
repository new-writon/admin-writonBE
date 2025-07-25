package com.writon.admin.domain.service;

import com.writon.admin.domain.dto.request.challenge.ChallengeInfoRequestDto;
import com.writon.admin.domain.dto.request.challenge.CreateChallengeRequestDto;
import com.writon.admin.domain.dto.request.challenge.QuestionsRequestDto;
import com.writon.admin.domain.dto.response.challenge.ChallengeInfoResponseDto;
import com.writon.admin.domain.dto.response.challenge.QuestionsResponseDto;
import com.writon.admin.domain.dto.lcoal.SpecialQuestionDto;
import com.writon.admin.domain.dto.lcoal.ChallengeResponse;
import com.writon.admin.domain.dto.response.challenge.CreateChallengeResponseDto;
import com.writon.admin.domain.dto.lcoal.Status;
import com.writon.admin.domain.dto.lcoal.UserStatus;
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
import java.util.Map;
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

  // ========== CreateChallenge API ==========
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
    for (SpecialQuestionDto specialQuestionDto : requestDto.getSpecialQuestions()) {
      Keyword keyword = keywordRepository.save(new Keyword(specialQuestionDto.getKeyword()));

      for (String specialQuestion : specialQuestionDto.getQuestions()) {
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
    List<Challenge> challenges = challengeRepository.findByOrganizationId(organization.getId());
    if (challenges.isEmpty()) {
      throw new CustomException(ErrorCode.CHALLENGE_NOT_FOUND);
    }

    List<ChallengeResponse> challengeList = challenges.stream()
        .map(entity -> new ChallengeResponse(entity.getId(), entity.getName()))
        .collect(Collectors.toList());

    return new CreateChallengeResponseDto(challengeList);
  }

  // ========== Get Dashboard API ==========
  public List<UserStatus> getDashboard(Long challengeId) {
    // 1. 챌린지 날짜 리스트 추출
    List<ChallengeDay> challengeDayList = challengeDayRepository.findByChallengeId(challengeId);

    if (challengeDayList.isEmpty()) {
      throw new CustomException(ErrorCode.CHALLENGE_DAY_NOT_FOUND);
    }

    challengeDayList = challengeDayList.stream()
        .sorted(Comparator.comparing(ChallengeDay::getDay))
        .toList();

    // 2. 챌린지 참여 유저 리스트 추출
    List<UserChallenge> userChallengeList = userChallengeRepository.findByChallengeId(challengeId);

    // 3. 유저별 참여여부 확인
    List<UserStatus> statusTable = new ArrayList<>();

    for (UserChallenge userChallenge : userChallengeList) {
      List<Status> statusList = new ArrayList<>();
      List<UserTemplate> userTemplateList = userTemplateRepository.findByUserChallengeId(
              userChallenge.getId());

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
    List<Question> questionList = questionRepository.findByChallengeId(challengeId);
    if (questionList.isEmpty()) {
      throw new CustomException(ErrorCode.QUESTION_NOT_FOUND);
    }

    List<String> basicQuestions = questionList.stream()
        .filter(question -> question.getKeyword() == null)
        .map(Question::getQuestion)
        .toList();

    List<SpecialQuestionDto> specialQuestions = questionList.stream()
        .filter(question -> question.getKeyword() != null)
        .collect(Collectors.groupingBy(
            Question::getKeyword,
            LinkedHashMap::new,  // 순서를 유지하기 위해 LinkedHashMap 사용
            Collectors.mapping(Question::getQuestion, Collectors.toList())
        ))
        .entrySet().stream()
        .map(entry -> new SpecialQuestionDto(
            entry.getKey().getId(),
            entry.getKey().getKeyword(),
            entry.getValue()
        ))
        .toList();

    return new QuestionsResponseDto(basicQuestions, specialQuestions);
  }

  // ========== Get Info API ==========
  public ChallengeInfoResponseDto getInfo(Long challengeId) {
    // 1. 챌린지 기본 정보 가져오기
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

    // 2. 챌린지 날짜 정보 가져오기
    List<ChallengeDay> challengeDayList = challengeDayRepository.findByChallengeId(challengeId);

    if (challengeDayList.isEmpty()) {
      throw new CustomException(ErrorCode.CHALLENGE_DAY_NOT_FOUND);
    }

    return new ChallengeInfoResponseDto(
        challenge.getName(),
        challenge.getStartAt(),
        challenge.getFinishAt(),
        challengeDayList.stream().map(ChallengeDay::getDay).collect(Collectors.toList())
    );
  }

  // ========== Put Questions API ==========
  public QuestionsResponseDto putQuestions(Long challengeId, QuestionsRequestDto requestDto) {
    // 1. 챌린지 조회
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

    // 2. 질문 리스트 조회
    List<Question> questionList = questionRepository.findByChallengeId(challengeId);
    if (questionList.isEmpty()) {
      throw new CustomException(ErrorCode.QUESTION_NOT_FOUND);
    }

    // 3. 베이직 질문 처리
    List<String> requestBasicQuestions = requestDto.getBasicQuestions();
    List<Question> existingBasicQuestions = questionList.stream()
        .filter(q -> q.getKeyword() == null).toList();

    int basicMax = Math.max(requestBasicQuestions.size(), existingBasicQuestions.size());
    List<Question> toSaveBasic = new ArrayList<>();
    List<Question> toDeleteBasic = new ArrayList<>();

    for (int i = 0; i < basicMax; i++) {
      if (i < requestBasicQuestions.size() && i < existingBasicQuestions.size()) {
        // 3-1. 수정 필요 여부 확인
        Question existing = existingBasicQuestions.get(i);
        String newQuestion = requestBasicQuestions.get(i);
        if (!existing.getQuestion().equals(newQuestion)) {
          existing.setQuestion(newQuestion);
          toSaveBasic.add(existing);
        }
      } else if (i < requestBasicQuestions.size()) {
        // 3-2. 새로 추가해야 하는 경우
        toSaveBasic.add(new Question(requestBasicQuestions.get(i), "베이직 질문", challenge));
      } else {
        // 3-3. 삭제해야 하는 경우
        toDeleteBasic.add(existingBasicQuestions.get(i));
      }
    }

    // 3-4. 일괄적으로 DB에 반영
    questionRepository.deleteAll(toDeleteBasic);
    questionRepository.saveAll(toSaveBasic);

    // 4. 스페셜 질문 처리
    List<SpecialQuestionDto> requestSpecialQuestions = requestDto.getSpecialQuestions();

    Map<Long, List<Question>> existingSpecialQuestionMap = questionList.stream()
        .filter(q -> q.getKeyword() != null)
        .collect(Collectors.groupingBy(q -> q.getKeyword().getId()));

    List<Question> toSaveSpecial = new ArrayList<>();
    List<Question> toDeleteSpecial = new ArrayList<>();

    for (SpecialQuestionDto dto : requestSpecialQuestions) {
      Long keywordId = dto.getKeywordId();
      String keywordName = dto.getKeyword();
      List<String> newQuestions = dto.getQuestions();

      // 4-1. keyword와 그에 해당하는 질문들 전부 생성
      if (keywordId == null) {
        Keyword newKeyword = new Keyword(keywordName);
        keywordRepository.save(newKeyword);
        for (String q : newQuestions) {
          toSaveSpecial.add(new Question(q, "스페셜 질문", challenge, newKeyword));
        }

      } else {
        List<Question> existingQuestions = existingSpecialQuestionMap.getOrDefault(
            keywordId,
            new ArrayList<>()
        );

        int specialMax = Math.max(existingQuestions.size(), newQuestions.size());

        for (int i = 0; i < specialMax; i++) {
          // 4-2. 수정 필요 여부 확인
          if (i < newQuestions.size() && i < existingQuestions.size()) {
            Question existing = existingQuestions.get(i);
            String newQuestion = newQuestions.get(i);
            if (!existing.getQuestion().equals(newQuestion)) {
              existing.setQuestion(newQuestion);
              toSaveSpecial.add(existing);
            }
          } else if (i < newQuestions.size()) {
            // 4-3. 새로 추가해야 하는 경우
            toSaveSpecial.add(new Question(
                newQuestions.get(i),
                "스페셜 질문",
                challenge,
                existingQuestions.get(0).getKeyword()
            ));
          } else {
            // 4-4. 삭제해야 하는 경우
            toDeleteSpecial.add(existingQuestions.get(i));
          }
        }

        // 4-5. 처리된 키워드는 제거 (삭제 대상에서 제외됨)
        existingSpecialQuestionMap.remove(keywordId);
      }
    }

    // 4-6. 남은 키워드는 요청에 없는 것으로 삭제 대상
    List<Keyword> toDeleteKeywords = new ArrayList<>(keywordRepository.findAllById(
        existingSpecialQuestionMap.keySet()));

    // 4-7. 일괄적으로 DB에 반영
    questionRepository.deleteAll(toDeleteSpecial);
    questionRepository.saveAll(toSaveSpecial);
    keywordRepository.deleteAll(toDeleteKeywords);

    // 5. question 재조회 후 반환
    return getQuestions(challengeId);
  }

  // ========== Put Info API ==========
  public ChallengeInfoResponseDto putInfo(Long challengeId, ChallengeInfoRequestDto requestDto) {
    // 1. 챌린지 기본 정보 조회
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

    // 2. 챌린지 기본 정보 수정 및 저장
    challenge.setName(requestDto.getName());
    challenge.setStartAt(requestDto.getStartDate());
    challenge.setFinishAt(requestDto.getEndDate());
    Challenge editedChallenge = challengeRepository.save(challenge);

    // 3. 챌린지 날짜 정보 조회
    List<ChallengeDay> challengeDays = challengeDayRepository.findByChallengeId(challengeId);

    if (challengeDays.isEmpty()) {
      throw new CustomException(ErrorCode.CHALLENGE_DAY_NOT_FOUND);
    }

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
    List<ChallengeDay> editedChallengeDays = challengeDayRepository.findByChallengeId(challengeId);

    if (editedChallengeDays.isEmpty()) {
      throw new CustomException(ErrorCode.CHALLENGE_DAY_NOT_FOUND);
    }

    return new ChallengeInfoResponseDto(
        editedChallenge.getName(),
        editedChallenge.getStartAt(),
        editedChallenge.getFinishAt(),
        editedChallengeDays.stream().map(ChallengeDay::getDay).collect(Collectors.toList())
    );
  }
}
