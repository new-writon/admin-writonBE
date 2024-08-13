package com.writon.admin.domain.controller.auth;

import com.writon.admin.domain.dto.request.auth.LoginRequestDto;
import com.writon.admin.domain.dto.request.auth.ReissueRequestDto;
import com.writon.admin.domain.dto.request.auth.SignUpRequestDto;
import com.writon.admin.domain.dto.response.auth.LoginResponseDto;
import com.writon.admin.domain.dto.response.auth.ReissueResponseDto;
import com.writon.admin.domain.dto.response.auth.SignUpResponseDto;
import com.writon.admin.domain.service.AuthService;
import com.writon.admin.global.response.SuccessDto;
import lombok.RequiredArgsConstructor;
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

  @PostMapping("/signup")
  public SuccessDto<SignUpResponseDto> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
    SignUpResponseDto signUpResponseDto = authService.signup(signUpRequestDto);

    return new SuccessDto<>(signUpResponseDto);
  }

  @PostMapping("/login")
  public SuccessDto<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
    LoginResponseDto loginResponseDto = authService.login(loginRequestDto);

    return new SuccessDto<>(loginResponseDto);
  }

  @PostMapping("/reissue")
  public SuccessDto<ReissueResponseDto> reissue(@RequestBody ReissueRequestDto reissueRequestDto) {
    ReissueResponseDto reissueResponseDto = authService.reissue(reissueRequestDto);

    return new SuccessDto<>(reissueResponseDto);
  }

  @DeleteMapping("/logout")
  public SuccessDto<Void> logout() {
    authService.logout();

    return new SuccessDto<>();
  }

}
