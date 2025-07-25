package com.writon.admin.domain.service;

import com.writon.admin.domain.entity.challenge.Challenge;
import com.writon.admin.domain.entity.organization.Organization;
import com.writon.admin.domain.util.TokenUtil;
import com.writon.admin.global.error.CustomException;
import com.writon.admin.global.error.ErrorCode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.CreateSmtpEmail;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailMessageVersions;
import sibModel.SendSmtpEmailTo1;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final TokenUtil tokenUtil;

  @Value("${email.apiKey}")
  private String BREVO_API_KEY;

  @Value("${email.templateId}")
  private Long BREVO_TEMPLATE_ID;

  public void sendEmail(Challenge challenge, List<String> emailList) {
    Organization organization = tokenUtil.getOrganization();
    String baseUrl = "https://www.writon.co.kr/login";
    String link = String.format(
        "%s?organization=%s&challengeId=%s", baseUrl,
        encodeURIComponent(organization.getName()),
        encodeURIComponent(String.valueOf(challenge.getId()))
    );

    ApiClient defaultClient = Configuration.getDefaultApiClient();

    ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
    apiKey.setApiKey(BREVO_API_KEY);

    // 수신자 리스트 구성
    List<SendSmtpEmailMessageVersions> messageVersions = new ArrayList<>();

    for (String email : emailList) {
      messageVersions.add(new SendSmtpEmailMessageVersions()
          .to(List.of(new SendSmtpEmailTo1().email(email)))
          .params(Map.of(
              "ORGANIZATION", organization.getName(),
              "CHALLENGE", challenge.getName(),
              "EMAIL", email,
              "LINK", link
          )));
    }

    TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
    SendSmtpEmail sendSmtpEmail = new SendSmtpEmail()
        // 템플릿 종류
        .templateId(BREVO_TEMPLATE_ID)
        // 동적 param값 설정
        .messageVersions(messageVersions);

    try {
      CreateSmtpEmail result = apiInstance.sendTransacEmail(sendSmtpEmail);
      log.info("Succeded to send Email: {}", result);
    } catch (ApiException e) {
      log.error("Failed to send Email");
      throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
    }
  }

  private String encodeURIComponent(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

}