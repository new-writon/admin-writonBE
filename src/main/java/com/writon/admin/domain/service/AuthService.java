package com.writon.admin.domain.service;

import com.writon.admin.domain.dto.request.auth.LoginRequestDto;
import com.writon.admin.domain.dto.request.auth.SignUpRequestDto;
import com.writon.admin.domain.dto.request.auth.TokenRequestDto;
import com.writon.admin.domain.dto.response.auth.SignUpResponseDto;
import com.writon.admin.domain.entity.organization.AdminUser;
import com.writon.admin.domain.entity.token.RefreshToken;
import com.writon.admin.domain.repository.organization.AdminUserRepository;
import com.writon.admin.domain.repository.token.RefreshTokenRepository;
import com.writon.admin.global.config.auth.TokenDto;
import com.writon.admin.global.config.auth.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AdminUserRepository adminUserRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenProvider tokenProvider;

  public SignUpResponseDto signup(SignUpRequestDto signUpRequestDto) {
    if (adminUserRepository.existsByIdentifier(signUpRequestDto.getIdentifier())) {
      throw new RuntimeException("이미 가입되어 있는 유저입니다");
    }

    AdminUser adminUser = signUpRequestDto.toAdminUser(passwordEncoder);
    return SignUpResponseDto.of(adminUserRepository.save(adminUser));
  }

  public TokenDto login(LoginRequestDto loginRequestDto) {

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

    // 5. 토큰 발급
    return tokenDto;
  }


}
