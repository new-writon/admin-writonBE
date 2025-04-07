package com.writon.admin.domain.service;

import com.writon.admin.domain.dto.request.auth.LoginRequestDto;
import com.writon.admin.domain.dto.request.auth.SignUpRequestDto;
import com.writon.admin.domain.dto.request.auth.ReissueRequestDto;
import com.writon.admin.domain.dto.response.auth.LoginResponseDto;
import com.writon.admin.domain.dto.response.auth.ReissueResponseDto;
import com.writon.admin.domain.dto.response.auth.SignUpResponseDto;
import com.writon.admin.domain.dto.wrapper.auth.LoginResponseWrapper;
import com.writon.admin.domain.entity.challenge.Challenge;
import com.writon.admin.domain.entity.lcoal.ChallengeResponse;
import com.writon.admin.domain.entity.organization.AdminUser;
import com.writon.admin.domain.entity.organization.Organization;
import com.writon.admin.domain.repository.challenge.ChallengeRepository;
import com.writon.admin.domain.repository.organization.AdminUserRepository;
import com.writon.admin.domain.repository.organization.OrganizationRepository;
import com.writon.admin.global.config.auth.TokenDto;
import com.writon.admin.global.config.auth.TokenProvider;
import com.writon.admin.global.error.CustomException;
import com.writon.admin.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AdminUserRepository adminUserRepository;
  private final OrganizationRepository organizationRepository;
  private final RefreshTokenService refreshTokenService;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final TokenProvider tokenProvider;
  private final ChallengeRepository challengeRepository;


  // ========== SignUp API ==========
  public SignUpResponseDto signup(SignUpRequestDto signUpRequestDto) {
    if (adminUserRepository.existsByIdentifier(signUpRequestDto.getIdentifier())) {
      throw new CustomException(ErrorCode.ETC_ERROR);
    }

    AdminUser encodedAdminUser = signUpRequestDto.toAdminUser(passwordEncoder);
    AdminUser adminUser = adminUserRepository.save(encodedAdminUser);

    return new SignUpResponseDto(adminUser.getId(), adminUser.getIdentifier());
  }

  // ========== Login API ==========
  public LoginResponseWrapper login(LoginRequestDto loginRequestDto) {

    // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
    UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

    // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
    // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
    String identifier;
    try {
      Authentication authentication = authenticationManagerBuilder.getObject()
          .authenticate(authenticationToken);
      identifier = authentication.getName();
    } catch (BadCredentialsException e) {
      throw new CustomException(ErrorCode.BAD_CREDENTIAL_ACCESS);
    } catch (DisabledException e) {
      throw new CustomException(ErrorCode.DISABLED_USER);
    } catch (LockedException e) {
      throw new CustomException(ErrorCode.LOCKED_USER);
    }

    // 3. 인증 정보를 기반으로 JWT 토큰 생성
    TokenDto tokenDto = tokenProvider.createToken(identifier);

    // 4. RefreshToken 저장 (이미 존재한다면 전부 제거 후 저장)
    while (refreshTokenService.hasKey(identifier)) {
      refreshTokenService.deleteRefreshToken(identifier);
    }
    refreshTokenService.saveRefreshToken(identifier, tokenDto.getRefreshToken());

    // 5. 해당 Organization 정보 가져오기
    AdminUser adminUser = adminUserRepository.findByIdentifier(identifier)
        .orElseThrow(() -> new UsernameNotFoundException(" -> 데이터베이스에서 찾을 수 없습니다."));
    Optional<Organization> organization = organizationRepository.findByAdminUserId(adminUser.getId());

    // 7. 챌린지 정보 가져오기
    List<Challenge> challenges = Collections.emptyList();
    if (organization.isPresent()) {
      challenges = challengeRepository.findByOrganizationId(organization.get().getId())
          .orElse(Collections.emptyList()); // 데이터가 없을 때 빈 리스트 반환
    }
    List<ChallengeResponse> challengeList = challenges.stream()
        .map(entity -> new ChallengeResponse(entity.getId(), entity.getName()))
        .toList();

    LoginResponseDto loginResponseDto = new LoginResponseDto(
        organization.isPresent(),
        organization.map(Organization::getId).orElse(null),
        organization.map(Organization::getName).orElse(null),
        organization.map(Organization::getThemeColor).orElse(null),
        organization.map(Organization::getLogo).orElse(null),
        challengeList
    );

    // 8. Response 전달
    return new LoginResponseWrapper(tokenDto, loginResponseDto);
  }

  // ========== Reissue API ==========
  public String reissue(String accessToken, String refreshToken) {

    // 1. Access Token 에서 identifier 가져오기
    String identifier = tokenProvider.getIdentifier(accessToken)
        .getSubject();

    // 2. Refresh Token 일치여부 확인
    String storedRefreshToken = refreshTokenService.getRefreshToken(identifier);

    if (refreshToken == null || !refreshToken.equals(storedRefreshToken)) {
      throw new CustomException(ErrorCode.REFRESH_TOKEN_INCONSISTENCY);
    }

    // 3. 새로운 Access Token 생성
    return tokenProvider.createAccessToken(identifier);
  }

  // ========== Logout API ==========
  public void logout() {

    // 1. 사용자 이름을 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String identifier = authentication.getName();

    // 2. RefreshToken 삭제
    refreshTokenService.deleteRefreshToken(identifier);
  }
}
