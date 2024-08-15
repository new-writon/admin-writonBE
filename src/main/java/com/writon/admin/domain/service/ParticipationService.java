package com.writon.admin.domain.service;

import com.writon.admin.domain.entity.challenge.Challenge;
import com.writon.admin.domain.entity.lcoal.ParticipationInfo;
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
import java.util.List;
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

  public List<String> getEmailList(Long challengeId) {

    List<Email> emailList = emailRepository.findByChallengeId(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

    return emailList.stream().map(Email::getEmail).toList();
  }

}
