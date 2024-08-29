package com.writon.admin.global.error;

import com.writon.admin.domain.entity.organization.Organization;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode {

  // error
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "잘못된 요청입니다"), // 400 Bad Request
  FORBIDDEN(HttpStatus.FORBIDDEN, "403", "권한이 없습니다"), // 403 Forbidden
  NOT_FOUND(HttpStatus.NOT_FOUND, "404", "사용자를 찾을 수 없습니다"), // 404 Not Found
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "405", "허용되지 않은 메소드입니다"), // 405 Method Not Allowed
  CONFLICT(HttpStatus.CONFLICT, "409", "이미 가입한 사용자입니다"), // 409 Conflict
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버에 오류가 발생하였습니다"), // 500 Internal Server Error
  ETC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0314", "사용자 지정 오류"),


  // auth
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "A01", "사용자를 찾을 수 없습니다"),
  TOKEN_ERROR(HttpStatus.BAD_REQUEST, "A02", "만료된 토큰입니다."),
  LOGOUT_USER(HttpStatus.NOT_FOUND, "410", "로그아웃한 유저입니다."),

  // organization
  ORGANIZATION_NOT_FOUND(HttpStatus.NOT_FOUND, "O01", "조직 정보를 찾을 수 없습니다"),

  // challenge
  CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "C01", "챌린지 정보를 찾을 수 없습니다");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

}
