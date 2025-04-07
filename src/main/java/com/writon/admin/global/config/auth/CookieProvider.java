package com.writon.admin.global.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieProvider {

  private static final long ACCESS_TOKEN_MAX_AGE = 60 * (60 + 30) ;     // 1시간 30분 (단위: s)
  private static final long REFRESH_TOKEN_MAX_AGE = 60 * 60 * 24;       // 1일 (단위: s)

  // AT, RT 쿠키헤더를 생성하는 메서드
  public HttpHeaders createTokenCookie(TokenDto tokenDto) {
    ResponseCookie accessTokenCookie = createCookie(
        "accessToken",
        tokenDto.getAccessToken(),
        ACCESS_TOKEN_MAX_AGE
    );
    ResponseCookie refreshTokenCookie = createCookie(
        "refreshToken",
        tokenDto.getRefreshToken(),
        REFRESH_TOKEN_MAX_AGE
    );

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

    return headers;
  }


  // AT 쿠키헤더만 생성하는 메서드
  public HttpHeaders createTokenCookie(String accessToken) {
    ResponseCookie accessTokenCookie = createCookie(
        "accessToken",
        accessToken,
        ACCESS_TOKEN_MAX_AGE
    );

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

    return headers;
  }


  // AT, RT 쿠키헤더를 제거하는 메서드
  public HttpHeaders removeTokenCookie() {
    ResponseCookie accessTokenCookie = createCookie(
        "accessToken",
        "",
        0
    );
    ResponseCookie refreshTokenCookie = createCookie(
        "refreshToken",
        "",
        0
    );

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

    return headers;
  }


  // Cookie를 생성하는 메서드
  private ResponseCookie createCookie(String name, String value, long maxAge) {
    return ResponseCookie.from(name, value)
        .httpOnly(true)       // HttpOnly 속성 적용
        .secure(true)        // HTTPS에서만 전송 (로컬 테스트 시 false 설정)
        .path("/")            // 모든 경로에서 사용 가능하도록 설정
        .sameSite("Strict")   // CSRF 방지
        .maxAge(maxAge)       // 쿠키 만료 시간 설정
        .build();
  }
}