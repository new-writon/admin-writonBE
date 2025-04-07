package com.writon.admin.global.config.auth;

import com.writon.admin.global.error.ErrorCode;
import com.writon.admin.global.error.ExceptionResponseHandler;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final TokenProvider tokenProvider;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ExceptionResponseHandler exceptionResponseHandler = new ExceptionResponseHandler();
  private final List<String> excludedPaths = Arrays.asList("/auth/login","/auth/signup");

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    // 로그인, 회원가입 API URL이 포함되는지 확인하는 함수
    String path = request.getRequestURI();
    return excludedPaths.stream().anyMatch(path::equals);
  }

  // 실제 필터링 로직은 doFilterInternal 에 들어감
  // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws IOException, ServletException {

    // 1. Request Header 에서 토큰을 꺼냄
    String accessToken = extractTokenFromCookie(request, "accessToken");
    String refreshToken = extractTokenFromCookie(request, "refreshToken");

    // 2. 토큰의 존재여부 검사
    if (!StringUtils.hasText(accessToken)){
      exceptionResponseHandler.setResponse(response, ErrorCode.ACCESS_TOKEN_NOT_FOUND);
      return;

    // 3. accessToken 유효성 검사
    } else if (tokenProvider.validateToken(accessToken, request)) {

      // 4. 장시간 사용자 확인 (access: O, refresh: X)
      Claims identifier = tokenProvider.getIdentifier(accessToken);

      if (refreshToken == null || redisTemplate.opsForValue().get(identifier.getSubject()) == null) {
        exceptionResponseHandler.setResponse(response, ErrorCode.REFRESH_TOKEN_EXPIRATION);
        return;
      }

      // 5. 정상적인 인증확인 (access: O, refresh: O)
      Authentication authentication = tokenProvider.getAuthentication(identifier);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }


  // Cookie로부터 Token을 추출하는 메서드
  public String extractTokenFromCookie(HttpServletRequest request, String tokenName) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (tokenName.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}
