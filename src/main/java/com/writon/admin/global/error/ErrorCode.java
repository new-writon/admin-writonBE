package com.writon.admin.global.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode {

  // error
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "잘못된 요청입니다"), // 400 Bad Request
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "권한이 없습니다"), // 401 Unauthorized
  FORBIDDEN(HttpStatus.FORBIDDEN, "403", "잘못된 요청입니다"), // 403 Forbidden
  NOT_FOUND(HttpStatus.NOT_FOUND, "404", "사용자를 찾을 수 없습니다"), // 404 Not Found
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "405", "허용되지 않은 메소드입니다"), // 405 Method Not Allowed
  CONFLICT(HttpStatus.CONFLICT, "409", "이미 가입한 사용자입니다"), // 409 Conflict
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버에 오류가 발생하였습니다"), // 500 Internal Server Error
  ETC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0314", "사용자 지정 오류"),

  // auth
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "A01", "사용자를 찾을 수 없습니다"),
  NOT_CORRECT_USER(HttpStatus.BAD_REQUEST, "A02", "아이디나 비밀번호가 잘못되었습니다"),
  REFRESH_TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED, "A03", "만료된 토큰입니다"),
  ACCESS_TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED, "A04", "토큰 재발급을 요청해주세요"),
  REFRESH_TOKEN_INCONSISTENCY(HttpStatus.NOT_FOUND, "A05", "토큰이 일치하지 않습니다"),

  // organization
  ORGANIZATION_NOT_FOUND(HttpStatus.NOT_FOUND, "O01", "조직 정보를 찾을 수 없습니다"),

  // challenge
  CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "C01", "챌린지 정보를 찾을 수 없습니다"),

  // participation
  EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "P01", "중복되는 이메일이 존재합니다");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

}
