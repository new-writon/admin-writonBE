package com.writon.admin.global.response;

import lombok.Getter;

@Getter
public class SuccessDto<T> {

  private final int status;
  private final String message;
  private final T data;

  // 기본 생성자: 데이터 없이 초기화할 수 있음
  public SuccessDto() {
    this.status = 200;
    this.message = "success";
    this.data = null;
  }

  // 데이터가 있는 경우 사용할 생성자
  public SuccessDto(T data) {
    this.status = 200;
    this.message = "success";
    this.data = data;
  }
}
