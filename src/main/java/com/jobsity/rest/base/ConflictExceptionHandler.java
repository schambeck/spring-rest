package com.jobsity.rest.base;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
public class ConflictExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest request) {
        ErrorData responseBody = new ErrorData("Entity already exists");
        return handleExceptionInternal(exception, responseBody, new HttpHeaders(), CONFLICT, request);
    }

}
