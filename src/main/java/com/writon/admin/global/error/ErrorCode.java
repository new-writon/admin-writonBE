package com.writon.admin.global.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode {

  // user
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U01", "사용자를 찾을 수 없습니다"),

  // book
  BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "B01", "책을 찾을 수 없습니다"),
  BOOK_ALREADY_LOANED(HttpStatus.BAD_REQUEST, "B02", "현재 대여중인 책입니다"),
  BOOK_NOT_LOANED(HttpStatus.BAD_REQUEST, "B03", "이미 반납된 책입니다"),

  // error
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "잘못된 요청입니다"), // 400 Bad Request
  FORBIDDEN(HttpStatus.FORBIDDEN, "403", "권한이 없습니다"), // 403 Forbidden
  NOT_FOUND(HttpStatus.NOT_FOUND, "404", "사용자를 찾을 수 없습니다"), // 404 Not Found
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "405", "허용되지 않은 메소드입니다"), // 405 Method Not Allowed
  CONFLICT(HttpStatus.CONFLICT, "409", "이미 가입한 사용자입니다"), // 409 Conflict
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버에 오류가 발생하였습니다"), // 500 Internal Server Error

  // auth
  LOGOUT_USER(HttpStatus.NOT_FOUND, "410", "로그아웃한 유저입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

}
