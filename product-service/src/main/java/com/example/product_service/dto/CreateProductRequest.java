package com.example.product_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(
            max = 100,
            message = "Product name cannot exceed 100 characters"
    )
    private String name;

    @NotNull(message = "Product price is required")
    @DecimalMin(
            value = "0.01",
            message = "Product price must be greater than 0"
    )
    private BigDecimal price;

    @NotBlank(message = "Product category is required")
    @Size(
            max = 100,
            message = "Product category cannot exceed 100 characters"
    )
    private String category;
}