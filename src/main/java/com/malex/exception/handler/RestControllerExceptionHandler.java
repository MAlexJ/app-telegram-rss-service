package com.malex.exception.handler;

import com.malex.exception.Md5HashCalculationException;
import com.malex.exception.RssReaderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionHandler {

  @ExceptionHandler({Md5HashCalculationException.class, RssReaderException.class})
  public ResponseEntity<Object> handleStudentNotFoundException(Exception exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }
}
