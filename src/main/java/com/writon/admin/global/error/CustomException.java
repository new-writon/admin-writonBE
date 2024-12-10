package com.writon.admin.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode responseCode;

  public CustomException(ErrorCode responseCode) {
    super(responseCode.getMessage()); // 메시지를 부모 클래스에 전달
    this.responseCode = responseCode;
  }
}
