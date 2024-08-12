package com.writon.admin.global.response;

import com.writon.admin.global.error.ErrorCode;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDto {

  private int status;
  private String code;
  private String message;

  public static ResponseEntity<ErrorDto> toResponseEntity(ErrorCode e) {

    return ResponseEntity
        .status(e.getHttpStatus())
        .body(ErrorDto.builder()
            .status(e.getHttpStatus().value())
            .code(e.getCode())
            .message(e.getMessage())
            .build());
  }
}

