package com.valkoluv.product_catalog_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<Map<String, String>> handleIllegalOperationException(IllegalOperationException ex) {
        return new ResponseEntity<>(
                Map.of("error", "Unavailable For Legal Reasons (451)", "message", ex.getMessage()),
                HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                Map.of("error", "Not Found (404)", "message", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}