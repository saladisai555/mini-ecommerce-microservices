package com.example.product_service.exception;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------------------------------------------------------
    // PRODUCT NOT FOUND
    // ---------------------------------------------------------

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>>
    handleProductNotFound(
            ProductNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "PRODUCT_NOT_FOUND",
                        "message", ex.getMessage()
                ));
    }


    // ---------------------------------------------------------
    // REQUEST BODY VALIDATION
    // ---------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }


    // ---------------------------------------------------------
    // PATH VARIABLE / REQUEST PARAMETER VALIDATION
    // ---------------------------------------------------------

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>>
    handleConstraintViolation(
            ConstraintViolationException ex) {

        Map<String, String> errors =
                new HashMap<>();

        ex.getConstraintViolations()
                .forEach(violation -> {

                    String property =
                            violation
                                    .getPropertyPath()
                                    .toString();

                    String message =
                            violation
                                    .getMessage();

                    errors.put(
                            property,
                            message
                    );
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}