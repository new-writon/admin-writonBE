package com.writon.admin.domain.service;

import com.writon.admin.domain.entity.challenge.Challenge;
import com.writon.admin.domain.entity.organization.Organization;
import com.writon.admin.domain.util.TokenUtil;
import com.writon.admin.global.error.CustomException;
import com.writon.admin.global.error.ErrorCode;
import jakarta.mail.internet.MimeMessage;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.thymeleaf.context.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender javaMailSender;
  private final SpringTemplateEngine templateEngine;
  private final TokenUtil tokenUtil;

  @Async
  public void sendEmail(Challenge challenge, String email) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    Organization organization = tokenUtil.getOrganization();

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
      mimeMessageHelper.setTo(email);
      mimeMessageHelper.setSubject(String.format(
          "[Writon] %s의 챌린지에 참여해보세요",
          organization.getName()
      )); // 메일 제목
      mimeMessageHelper.setText(
          setContext(organization.getName(), challenge.getName(), challenge.getId(), email),
          true
      ); // 메일 본문 내용, HTML 여부
      javaMailSender.send(mimeMessage);

      log.info("Succeeded to send Email");
    } catch (Exception e) {
      log.info("Failed to send Email");
      throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
    }
  }

  //thymeleaf를 통한 html 적용
  public String setContext(String organization, String challenge, Long challengeId, String email) {
    Context context = new Context();
    context.setVariable("organization", organization);
    context.setVariable("challenge", challenge);
    context.setVariable("email", email);
    context.setVariable("challengeId", challengeId);

    String baseUrl = "https://www.writon.co.kr/login";
    String link = String.format("%s?organization=%s&challengeId=%s", baseUrl,
        encodeURIComponent(organization),
        encodeURIComponent(String.valueOf(challengeId)));
    context.setVariable("link", link);

    return templateEngine.process("participate_card", context);
  }

  private String encodeURIComponent(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
