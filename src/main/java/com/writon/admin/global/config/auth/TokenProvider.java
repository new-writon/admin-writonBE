package com.writon.admin.global.config.auth;

import com.writon.admin.global.error.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;          // 1시간 (단위: ms)
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;    // 1일(24시간) (단위: ms)

  private final Key key;

  public TokenProvider(@Value("${jwt.secret}") String secret) {
    // 빈이 생성되고 주입을 받은 후에 secret값을 Base64 Decode해서 key 변수에 할당하기 위해
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
  public TokenDto createToken(String identifier) {

    // Token 생성
    String accessToken = createAccessToken(identifier);
    String refreshToken = createRefreshToken();

    return TokenDto.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  // AccessToken 을 생성하는 메서드
  public String createAccessToken(String identifier) {

    // 토큰의 expire 시간을 설정
    long now = (new Date()).getTime();

    // AccessToken 생성
     return Jwts.builder()
        .setSubject(identifier)     // payload "sub": "name"
        .claim(AUTHORITIES_KEY, "admin")      // payload: "auth: "ROLE_USER"
        .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))      // payload "exp": 151621022 (ex)
        .signWith(key, SignatureAlgorithm.HS512)  // header "alg": "HS512"
        .compact();
  }

  // RefreshToken 을 생성하는 메서드
  public String createRefreshToken() {

    // 토큰의 expire 시간을 설정
    long now = (new Date()).getTime();

    // RefreshToken 생성
    return Jwts.builder()
        .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
  public Authentication getAuthentication(Claims claims) {
    // 토큰 복호화
    if (claims.get(AUTHORITIES_KEY) == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    // 클레임에서 권한 정보 가져오기
    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get("auth").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    // UserDetails 객체를 만들어서 Authentication 리턴
    UserDetails principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  //토큰에서 Identifier 추출
  public Claims getIdentifier(String accessToken) {
    try {
      return Jwts
          .parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(accessToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  // 토큰의 유효성 검증을 수행
  public boolean validateToken(String token, HttpServletRequest request) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
      // 토큰 재발급, 로그아웃의 경우 유효성 통과해서 로직 실행하도록 설정
      if (request.getRequestURI().equals("/auth/reissue") || request.getRequestURI().equals("/auth/logout")) {
        return true;
      } else {
        // Token 만료 예외처리
        request.setAttribute("exception", ErrorCode.ACCESS_TOKEN_EXPIRATION.getCode());
      }
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
      request.setAttribute("exception", ErrorCode.UNAUTHORIZED_TOKEN.getCode());
    }
    return false;
  }
}
