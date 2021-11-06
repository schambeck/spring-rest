package com.jobsity.rest.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
public class ConflictExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<ErrorData> handleConflictException(ConflictException exception) {
        ErrorData responseBody = new ErrorData(exception.getMessage());
        return ResponseEntity.status(CONFLICT).body(responseBody);
    }

}
