package com.writon.admin.domain.controller;

import com.writon.admin.domain.dto.request.auth.LoginRequestDto;
import com.writon.admin.domain.dto.request.auth.ReissueRequestDto;
import com.writon.admin.domain.dto.request.auth.SignUpRequestDto;
import com.writon.admin.domain.dto.response.auth.LoginResponseDto;
import com.writon.admin.domain.dto.response.auth.ReissueResponseDto;
import com.writon.admin.domain.dto.response.auth.SignUpResponseDto;
import com.writon.admin.domain.dto.wrapper.auth.LoginResponseWrapper;
import com.writon.admin.domain.service.AuthService;
import com.writon.admin.global.config.auth.CookieProvider;
import com.writon.admin.global.response.SuccessDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final CookieProvider cookieProvider;

  // ========== 회원가입 API ==========
  @PostMapping("/signup")
  public SuccessDto<SignUpResponseDto> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
    SignUpResponseDto signUpResponseDto = authService.signup(signUpRequestDto);

    return new SuccessDto<>(signUpResponseDto);
  }


  // ========== 로그인 API ==========
  @PostMapping("/login")
  public ResponseEntity<SuccessDto<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
    LoginResponseWrapper loginResponseWrapper = authService.login(loginRequestDto);
    HttpHeaders cookieHeaders = cookieProvider.createTokenCookie(loginResponseWrapper.getTokenDto());

    return new SuccessDto<>(loginResponseWrapper.getLoginResponseDto()).toResponseEntity(
        cookieHeaders);
  }


  // ========== 토큰 재발급 API ==========
  @PostMapping("/reissue")
  public ResponseEntity<SuccessDto<Object>> reissue(
      @CookieValue(value = "accessToken", required = false) String accessToken,
      @CookieValue(value = "refreshToken", required = false) String refreshToken
  ) {
    String newAccessToken = authService.reissue(accessToken, refreshToken);
    HttpHeaders cookieHeaders = cookieProvider.createTokenCookie(newAccessToken);

    return new SuccessDto<>().toResponseEntity(cookieHeaders);
  }


  // ========== 로그아웃 API ==========
  @DeleteMapping("/logout")
  public ResponseEntity<SuccessDto<Object>> logout() {
    authService.logout();
    HttpHeaders cookieHeaders = cookieProvider.removeTokenCookie();

    return new SuccessDto<>().toResponseEntity(cookieHeaders);
  }

    return new SuccessDto<>();
  }

}
