package com.writon.admin.domain.service;

import com.writon.admin.domain.entity.challenge.Challenge;
import com.writon.admin.domain.dto.lcoal.ParticipationInfo;
import com.writon.admin.domain.entity.challenge.Email;
import com.writon.admin.domain.entity.user.Affiliation;
import com.writon.admin.domain.entity.user.User;
import com.writon.admin.domain.entity.user.UserChallenge;
import com.writon.admin.domain.repository.activity.CommentRepository;
import com.writon.admin.domain.repository.activity.UserTemplateRepository;
import com.writon.admin.domain.repository.challenge.ChallengeDayRepository;
import com.writon.admin.domain.repository.challenge.ChallengeRepository;
import com.writon.admin.domain.repository.challenge.EmailRepository;
import com.writon.admin.domain.repository.small_talk.SmallTalkRepository;
import com.writon.admin.domain.repository.user.UserChallengeRepository;
import com.writon.admin.global.error.CustomException;
import com.writon.admin.global.error.ErrorCode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipationService {

  private final EmailRepository emailRepository;
  private final UserChallengeRepository userChallengeRepository;
  private final SmallTalkRepository smallTalkRepository;
  private final UserTemplateRepository userTemplateRepository;
  private final CommentRepository commentRepository;
  private final ChallengeDayRepository challengeDayRepository;
  private final ChallengeRepository challengeRepository;
  private final EmailService emailService;

  // ========== Get Email API ==========
  public List<String> getEmailList(Long challengeId) {

    List<Email> emailList = emailRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    return emailList.stream().map(Email::getEmail).toList();
  }

  // ========== Get ParticipationInfo API ==========
  public List<ParticipationInfo> getParticipationInfo(Long challengeId) {
    // 1. 챌린지 조회
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    // 2. 유저 챌린지 조회
    List<UserChallenge> userChallengeList = userChallengeRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    // 3. 유저 정보 조회
    List<ParticipationInfo> participationInfoList = new ArrayList<>();

    for (UserChallenge userChallenge : userChallengeList) {
      Affiliation affiliation = userChallenge.getAffiliation();
      User user = affiliation.getUser();

      List<UserChallenge> userChallenges = userChallengeRepository.findByAffiliationId(affiliation.getId())
          .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));
      String challenges = userChallenges.stream()
          .map(entity -> entity.getChallenge().getName())
          .collect(Collectors.joining(", "));

      int smallTalkCnt = smallTalkRepository.countByUserChallengeId(userChallenge.getId());
      int writingCnt = userTemplateRepository.countByUserChallengeId(userChallenge.getId());
      int commentCnt = commentRepository.countByAffiliationId(affiliation.getId());

      ParticipationInfo participationInfo = new ParticipationInfo(
          userChallenge.getId(),
          userChallenge.getWithdrawn(),
          affiliation.getNickname(),
          userChallenges.size(),
          challenges,
          challenge.getStartAt(),
          affiliation.getPosition(),
          affiliation.getCompany(),
          userChallenge.getCreatedAt(),
          user.getBank(),
          user.getAccountNumber(),
          user.getEmail(),
          userChallenge.getUserDeposit(),
          writingCnt,
          commentCnt,
          smallTalkCnt,
          affiliation.getPositionIntroduce()
      );

      participationInfoList.add(participationInfo);

    }

    return participationInfoList;
  }

  // ========== Post Withdrawal API ==========
  public List<ParticipationInfo> withdrawal(Long challengeId, List<Long> userChallengeIdList) {

    for (Long userChallengeId : userChallengeIdList) {
      UserChallenge userChallenge = userChallengeRepository.findById(userChallengeId)
          .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

      userChallenge.setWithdrawn(true);
      userChallengeRepository.save(userChallenge);
    }

    return getParticipationInfo(challengeId);
  }

  // ========== Post Participate API ==========
  public List<String> participate(Long challengeId, List<String> emailList) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    for (String email : emailList) {
      emailService.sendEmail(challenge, email);
      emailRepository.save(new Email(email, challenge));
    }

    List<Email> sendedEmailList = emailRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    return sendedEmailList.stream().map(Email::getEmail).toList();
  }

  public Boolean isParticipated(Long challengeId, List<String> emailList) {
    List<String> participatedEmailList = getEmailList(challengeId);

    // EmailList를 Set으로 변환하여 검색 속도 향상
    Set<String> participatedEmailSet = new HashSet<>(participatedEmailList);

    for (String email : emailList) {
      if (participatedEmailSet.contains(email)) {
        return true;
      }
    }

    return false;
  }
}
