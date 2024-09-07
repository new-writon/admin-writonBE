package com.writon.admin.global.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.writon.admin.global.error.CustomException;
import com.writon.admin.global.error.ErrorCode;
import com.writon.admin.global.response.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException {
    // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//    setResponse(response, ErrorCode.TOKEN_ERROR);
//  }
//
//  private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
//    response.setStatus(errorCode.getHttpStatus().value());
//    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//    response.setCharacterEncoding("UTF-8");
//    response.getWriter().write(objectMapper.writeValueAsString(ErrorDto.builder()
//        .status(errorCode.getHttpStatus().value())
//        .code(errorCode.getCode())
//        .message(errorCode.getMessage())
//        .build()));
  }
}