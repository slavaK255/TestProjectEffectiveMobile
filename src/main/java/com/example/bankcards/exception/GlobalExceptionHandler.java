package com.example.bankcards.exception;

import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Глобальный обработчик исключений для REST API.
 * Централизованно обрабатывает исключения, возникающие в контроллерах, и возвращает структурированные ответы.
 * Преобразует различные типы исключений в соответствующие HTTP-статусы и форматы ошибок.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid request argument: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request parameters");
    }

    @ExceptionHandler({
            WorkWithUserException.class,
            BlockCardException.class,
            InvalidExpiryDateException.class,
            InsufficientFundsException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleBusinessExceptions(RuntimeException e) {
        log.error("Business rule violation: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
    }

    @ExceptionHandler(CardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleCardNotFoundException(CardNotFoundException e) {
        log.error("Resource not found: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException e) {
        log.error("Incorrect password for user" + e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage());
    }

    @ExceptionHandler(OptimisticLockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleOptimisticLockException(OptimisticLockException e) {
        log.error("Concurrency conflict: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "Operation conflict detected. Please retry your request"
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleGeneralException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An internal server error occurred"
        );
    }
}