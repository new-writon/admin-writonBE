package com.writon.admin.domain.controller;

import com.writon.admin.domain.entity.lcoal.ParticipationInfo;
import com.writon.admin.domain.service.ParticipationService;
import com.writon.admin.global.response.SuccessDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participation")
@RequiredArgsConstructor
public class ParticipationController {

  private final ParticipationService participationService;

  @GetMapping("/email")
  public SuccessDto<List<String>> getEmailList(@RequestParam Long challengeId) {
    List<String> emailList = participationService.getEmailList(challengeId);

    return new SuccessDto<>(emailList);
  }


}
