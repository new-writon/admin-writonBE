package com.writon.admin.domain.controller.auth;

import com.writon.admin.domain.dto.request.auth.LoginRequestDto;
import com.writon.admin.domain.dto.request.auth.SignUpRequestDto;
import com.writon.admin.domain.dto.response.auth.SignUpResponseDto;
import com.writon.admin.domain.service.AuthService;
import com.writon.admin.global.config.auth.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public SuccessDto<SignUpResponseDto> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
    SignUpResponseDto signUpResponseDto = authService.signup(signUpRequestDto);

    return new SuccessDto<>(signUpResponseDto);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto loginRequestDto) {
    return ResponseEntity.ok(authService.login(loginRequestDto));
  }

}
