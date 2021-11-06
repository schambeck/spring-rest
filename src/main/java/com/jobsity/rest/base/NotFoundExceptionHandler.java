package com.jobsity.rest.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class NotFoundExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorData> handleNotFoundException(NotFoundException exception) {
        ErrorData responseBody = new ErrorData(exception.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(responseBody);
    }

}
