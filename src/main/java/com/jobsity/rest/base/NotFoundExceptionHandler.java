package com.jobsity.rest.base;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class NotFoundExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(RuntimeException exception, WebRequest request) {
        ErrorData responseBody = new ErrorData("Entity not found");
        return handleExceptionInternal(exception, responseBody, new HttpHeaders(), NOT_FOUND, request);
    }

}
