package com.writon.admin.global.response;

import lombok.Getter;

@Getter
public class SuccessDto<T> {
  private final int status;
  private final T data;
  private final String message;

  public SuccessDto(T data) {
    this.status = 200;
    this.data = data;
    this.message = "success";
  }
}
