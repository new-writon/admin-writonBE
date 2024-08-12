package com.writon.admin.global.response;

import lombok.Getter;

@Getter
public class SuccessDto<T> {
  private final int status;
  private final String message;
  private final T data;

  public SuccessDto(T data) {
    this.status = 200;
    this.message = "success";
    this.data = data;
  }
}
