package com.writon.admin.domain.service;

import com.writon.admin.domain.dto.request.auth.LoginRequestDto;
import com.writon.admin.domain.dto.request.auth.SignUpRequestDto;
import com.writon.admin.domain.dto.request.auth.ReissueRequestDto;
import com.writon.admin.domain.dto.response.auth.LoginResponseDto;
import com.writon.admin.domain.dto.response.auth.ReissueResponseDto;
import com.writon.admin.domain.dto.response.auth.SignUpResponseDto;
import com.writon.admin.domain.entity.organization.AdminUser;
import com.writon.admin.domain.entity.organization.Organization;
import com.writon.admin.domain.entity.token.RefreshToken;
import com.writon.admin.domain.repository.organization.AdminUserRepository;
import com.writon.admin.domain.repository.organization.OrganizationRepository;
import com.writon.admin.domain.repository.token.RefreshTokenRepository;
import com.writon.admin.global.config.auth.TokenDto;
import com.writon.admin.global.config.auth.TokenProvider;
import com.writon.admin.global.error.CustomException;
import com.writon.admin.global.error.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenProvider tokenProvider;


  // ========== SignUp API ==========
  public SignUpResponseDto signup(SignUpRequestDto signUpRequestDto) {
    if (adminUserRepository.existsByIdentifier(signUpRequestDto.getIdentifier())) {
      throw new RuntimeException("이미 가입되어 있는 유저입니다");
    }

    AdminUser encodedAdminUser = signUpRequestDto.toAdminUser(passwordEncoder);
    AdminUser adminUser = adminUserRepository.save(encodedAdminUser);

    return new SignUpResponseDto(adminUser.getId(), adminUser.getIdentifier());
  }

  // ========== Login API ==========
  public LoginResponseDto login(LoginRequestDto loginRequestDto) {

    // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
    UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

    // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
    // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);

    // 3. 인증 정보를 기반으로 JWT 토큰 생성
    TokenDto tokenDto = tokenProvider.createToken(authentication);

    // 4. RefreshToken 저장
    RefreshToken refreshToken = RefreshToken.builder()
        .token(tokenDto.getRefreshToken())
        .identifier(authentication.getName())
        .build();

    refreshTokenRepository.save(refreshToken);

    // 5. 해당 Organization 정보 가져오기
    AdminUser adminUser = adminUserRepository.findByIdentifier(authentication.getName())
        .orElseThrow(() -> new UsernameNotFoundException(" -> 데이터베이스에서 찾을 수 없습니다."));
    Optional<Organization> organization = organizationRepository.findByAdminUserId(adminUser.getId());

    // 6. Response 전달
    return new LoginResponseDto(
        tokenDto.getAccessToken(),
        tokenDto.getRefreshToken(),
        organization.isPresent(),
        organization.map(Organization::getId).orElse(null),
        organization.map(Organization::getName).orElse(null),
        organization.map(Organization::getThemeColor).orElse(null),
        organization.map(Organization::getLogo).orElse(null)
    );
  }

  // ========== Reissue API ==========
  public ReissueResponseDto reissue(ReissueRequestDto reissueRequestDto) {
    // 1. Refresh Token 검증
    if (!tokenProvider.validateToken(reissueRequestDto.getRefreshToken())) {
      throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
    }

    // 2. Access Token 에서 identifier 가져오기
    Authentication authentication = tokenProvider.getAuthentication(reissueRequestDto.getAccessToken());

    // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
    RefreshToken refreshToken = refreshTokenRepository.findByIdentifier(authentication.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.LOGOUT_USER));

    // 4. Refresh Token 일치하는지 검사
    if (!refreshToken.getToken().equals(reissueRequestDto.getRefreshToken())) {
      throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
    }

    // 5. 새로운 토큰 생성
    TokenDto tokenDto = tokenProvider.createToken(authentication);

    // 6. 저장소 정보 업데이트
    RefreshToken newRefreshToken = refreshToken.updateToken(tokenDto.getRefreshToken());
    refreshTokenRepository.save(newRefreshToken);

    // 7. 토큰 발급
    return new ReissueResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
  }

  // ========== Logout API ==========
  public void logout() {

    // 1. 사용자 이름을 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String identifier = authentication.getName();

    // 2. RefreshToken 삭제
    RefreshToken refreshToken = refreshTokenRepository.deleteByIdentifier(identifier)
        .orElseThrow(() -> new CustomException(ErrorCode.LOGOUT_USER));

  }
}
