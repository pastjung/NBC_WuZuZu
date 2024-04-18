package com.sparta.wuzuzu.global.exception;

import com.sparta.wuzuzu.domain.common.dto.ExceptionResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(Exception ex) {
        ExceptionResponse response = ExceptionResponse.builder()
            .msg(ex.getMessage())
            .httpCode(HttpStatus.BAD_REQUEST.value())
            .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(Exception ex) {
        ExceptionResponse response = ExceptionResponse.builder()
            .msg(ex.getMessage())
            .httpCode(HttpStatus.UNAUTHORIZED.value())
            .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
            .msg(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage())
            .httpCode(HttpStatus.BAD_REQUEST.value())
            .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ValidateUserException.class)
    public ResponseEntity<ExceptionResponse> handleValidateUserException(ValidateUserException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
            .msg(ex.getMessage())
            .httpCode(HttpStatus.FORBIDDEN.value())
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    @ExceptionHandler(ValidateAdminException.class)
    public ResponseEntity<ExceptionResponse> ValidationAdminException(MethodArgumentNotValidException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
            .msg(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage())
            .httpCode(HttpStatus.FORBIDDEN.value())
            .build();
        return ResponseEntity.badRequest().body(response);
    }


}
