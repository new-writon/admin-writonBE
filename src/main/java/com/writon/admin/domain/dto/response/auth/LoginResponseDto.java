package com.writon.admin.domain.dto.response.auth;

import com.writon.admin.domain.entity.lcoal.ChallengeResponse;
import com.writon.admin.domain.entity.organization.AdminUser;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

  private String accessToken;
  private String refreshToken;
  private boolean hasOrganization;
  private Long organizationId; // nullable
  private String organizationName; // nullable
  private String themeColor; // nullable
  private String organizationLogo; // nullable
  private List<ChallengeResponse> challengeList;

}
