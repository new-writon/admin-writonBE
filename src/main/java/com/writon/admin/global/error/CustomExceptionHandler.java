package com.writon.admin.global.error;

import com.writon.admin.global.response.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<ErrorDto> handleCustomException(CustomException e) {
    return ErrorDto.toResponseEntity(e.getResponseCode());
  }
}

