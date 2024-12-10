package com.writon.admin.global.error;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class ExceptionResponseHandler {
  public void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(errorCode.getHttpStatus().value());
    response.getWriter().write(String.format("{ \"status\": %d, \"code\": \"%s\", \"message\": \"%s\" }",
        errorCode.getHttpStatus().value(),
        errorCode.getCode(),
        errorCode.getMessage()));
  }
}
