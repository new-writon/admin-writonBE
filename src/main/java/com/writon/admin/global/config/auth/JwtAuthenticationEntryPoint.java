package com.writon.admin.global.config.auth;

import com.writon.admin.global.error.ErrorCode;
import com.writon.admin.global.error.ExceptionResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ExceptionResponseHandler exceptionResponseHandler;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException {
//    System.out.println("JwtAuthenticationEntryPoint");
    String exception = (String) request.getAttribute("exception");
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    if (exception != null) {
      if (exception.equals(ErrorCode.ACCESS_TOKEN_EXPIRATION.getCode())) {
        errorCode = ErrorCode.ACCESS_TOKEN_EXPIRATION;
      }

      if (exception.equals(ErrorCode.REFRESH_TOKEN_EXPIRATION.getCode())) {
        errorCode = ErrorCode.REFRESH_TOKEN_EXPIRATION;
      }

      if (exception.equals(ErrorCode.NOT_CORRECT_USER.getCode())) {
        errorCode = ErrorCode.NOT_CORRECT_USER;
      }
    }

    exceptionResponseHandler.setResponse(response, errorCode);
  }
}
