package com.denmiagkov.meter.infrastructure.in.exception_handling.handlers;

import com.denmiagkov.meter.application.service.exception.AuthenticationFailedException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.HasNoAdminStatusException;
import com.denmiagkov.meter.infrastructure.in.exception_handling.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String EXCEPTION_MESSAGE = "EXCEPTION OCCURRED: ";

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<String> handleException(AuthenticationFailedException exception) {
        LOG.error(EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HasNoAdminStatusException.class)
    public ResponseEntity<String> handleException(HasNoAdminStatusException exception) {
        LOG.error(EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException exception) {
        LOG.error(EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler()
    public ResponseEntity<String> handleException(RuntimeException exception) {
        LOG.error(EXCEPTION_MESSAGE, exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
