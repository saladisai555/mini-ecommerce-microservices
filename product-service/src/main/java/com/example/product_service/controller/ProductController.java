package com.example.product_service.controller;

import com.example.product_service.dto.CreateProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.dto.UpdateProductRequest;
import com.example.product_service.service.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService) {

        this.productService = productService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @Valid @RequestBody
            CreateProductRequest request) {

        return productService.createProduct(request);
    }


    @GetMapping
    public List<ProductResponse> getAllProducts() {

        return productService.getAllProducts();
    }


    @GetMapping("/{id}")
    public ProductResponse getProductById(
            @PathVariable
            @Positive(
                    message = "Product ID must be greater than 0"
            )
            Long id) {

        return productService.getProductById(id);
    }


    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable
            @Positive(
                    message = "Product ID must be greater than 0"
            )
            Long id,

            @Valid @RequestBody
            UpdateProductRequest request) {

        return productService.updateProduct(
                id,
                request
        );
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
            @PathVariable
            @Positive(
                    message = "Product ID must be greater than 0"
            )
            Long id) {

        productService.deleteProduct(id);
    }
}