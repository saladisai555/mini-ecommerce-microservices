package com.example.inventoryservice.exception;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // =========================================================
    // PRODUCT NOT FOUND
    // =========================================================

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>>
    handleProductNotFound(
            ProductNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error",
                        "PRODUCT_NOT_FOUND",

                        "message",
                        ex.getMessage()
                ));
    }


    // =========================================================
    // PRODUCT INACTIVE
    // =========================================================

    @ExceptionHandler(ProductInactiveException.class)
    public ResponseEntity<Map<String, String>>
    handleProductInactive(
            ProductInactiveException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error",
                        "PRODUCT_INACTIVE",

                        "message",
                        ex.getMessage()
                ));
    }


    // =========================================================
    // PRODUCT SERVICE UNAVAILABLE
    // =========================================================

    @ExceptionHandler(
            ProductServiceUnavailableException.class
    )
    public ResponseEntity<Map<String, String>>
    handleProductServiceUnavailable(
            ProductServiceUnavailableException ex) {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error",
                        "PRODUCT_SERVICE_UNAVAILABLE",

                        "message",
                        ex.getMessage()
                ));
    }


    // =========================================================
    // INVENTORY NOT FOUND
    // =========================================================

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<Map<String, String>>
    handleInventoryNotFound(
            InventoryNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error",
                        "INVENTORY_NOT_FOUND",

                        "message",
                        ex.getMessage()
                ));
    }


    // =========================================================
    // DUPLICATE INVENTORY
    // =========================================================

    @ExceptionHandler(
            InventoryAlreadyExistsException.class
    )
    public ResponseEntity<Map<String, String>>
    handleInventoryAlreadyExists(
            InventoryAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error",
                        "INVENTORY_ALREADY_EXISTS",

                        "message",
                        ex.getMessage()
                ));
    }


    // =========================================================
    // INSUFFICIENT STOCK
    // =========================================================

    @ExceptionHandler(
            InsufficientStockException.class
    )
    public ResponseEntity<Map<String, String>>
    handleInsufficientStock(
            InsufficientStockException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error",
                        "INSUFFICIENT_STOCK",

                        "message",
                        ex.getMessage()
                ));
    }


    // =========================================================
    // REQUEST BODY VALIDATION
    // =========================================================

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
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


    // =========================================================
    // PATH VARIABLE VALIDATION
    // =========================================================

    @ExceptionHandler(
            ConstraintViolationException.class
    )
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
                            violation.getMessage();

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