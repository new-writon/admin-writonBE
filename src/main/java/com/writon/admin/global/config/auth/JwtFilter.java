package com.writon.admin.global.config.auth;

import com.writon.admin.global.error.ErrorCode;
import com.writon.admin.global.error.ExceptionResponseHandler;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ExceptionResponseHandler exceptionResponseHandler = new ExceptionResponseHandler();

  // 실제 필터링 로직은 doFilterInternal 에 들어감
  // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws IOException, ServletException {

    // 1. Request Header 에서 토큰을 꺼냄
    String jwt = resolveToken(request);

    // 2. 토큰의 존재여부 & accessToken 유효성 검사
    if (tokenProvider.validateToken(jwt, request) && StringUtils.hasText(jwt)) {
      System.out.println("JWT Token 검증 통과");

      // 3. 로그아웃 유저 확인 (access: O, refresh: X)
      Claims identifier = tokenProvider.getIdentifier(jwt);

      if (redisTemplate.opsForValue().get(identifier.getSubject()) == null) {
        exceptionResponseHandler.setResponse(response, ErrorCode.REFRESH_TOKEN_EXPIRATION);
        return;
      }

      // 4. 정상적인 인증확인 (access: O, refresh: O)
      Authentication authentication = tokenProvider.getAuthentication(identifier);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  // Request Header 에서 토큰 정보 추출
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.split(" ")[1].trim();
    }
    return null;
  }
}
