package com.vinsguru.webfluxdemo.exceptionhandler;

import com.vinsguru.webfluxdemo.dto.InputFailedValidationResponse;
import com.vinsguru.webfluxdemo.exception.InputValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author debal
 */
@ControllerAdvice
public class InputValidationHandler {
    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<InputFailedValidationResponse> handle(InputValidationException ex) {
        final InputFailedValidationResponse inputFailedValidationResponse = InputFailedValidationResponse.builder()
                .errorCode(ex.getErrorCode())
                .input(ex.getInput())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(inputFailedValidationResponse);
    }
}
