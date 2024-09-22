package com.ing.broker.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleExceptions(Exception exception) {
    if (exception instanceof KnownException) {
      log.error("Known exception occurred!", exception);
      return ResponseEntity.status(((KnownException) exception).getStatus()).body(exception.getMessage());
    } else {
      log.error("Unexpected exception occurred!", exception);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected exception!");
    }
  }
}
