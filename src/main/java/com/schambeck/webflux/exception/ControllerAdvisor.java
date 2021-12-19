package com.schambeck.webflux.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
class ControllerAdvisor {

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Object> handleConflictException(DataIntegrityViolationException exception) {
        return createResponse(exception.getMessage(), CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        return createResponse(exception.getMessage(), NOT_FOUND);
    }

    private ResponseEntity<Object> createResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    ValidationErrorResponse handleConstraintValidationException(ConstraintViolationException e) {
        List<Violation> errors = e.getConstraintViolations().stream()
                .map(this::createViolation)
                .collect(toList());
        return new ValidationErrorResponse(errors);
    }

    private Violation createViolation(ConstraintViolation<?> error) {
        return new Violation(format("%s.%s", error.getRootBeanClass().getSimpleName(), error.getPropertyPath().toString()),
                error.getMessage(),
                error.getInvalidValue().toString());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    ValidationErrorResponse handleValidationException(WebExchangeBindException e) {
        var errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> createViolation(e, error))
                .collect(toList());
        return new ValidationErrorResponse(errors);
    }

    private Violation createViolation(WebExchangeBindException e, FieldError field) {
        Optional<Object> target = Optional.ofNullable(e.getTarget());
        BindingResult result = e.getBindingResult();
        Optional<Object> value = Optional.ofNullable(result.getRawFieldValue(field.getField()));
        return new Violation(format("%s.%s", target.map(p -> p.getClass().getSimpleName()).orElse(null), field.getField()),
                field.getDefaultMessage(),
                value.map(Object::toString).orElse(null));
    }

}
