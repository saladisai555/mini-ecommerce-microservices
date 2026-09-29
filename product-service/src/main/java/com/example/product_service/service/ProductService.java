package com.example.product_service.service;

import com.example.product_service.dto.CreateProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.dto.UpdateProductRequest;
import com.example.product_service.entity.Product;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository) {

        this.productRepository = productRepository;
    }


    // CREATE
    public ProductResponse createProduct(
            CreateProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());

        // Every new product starts as active
        product.setActive(true);

        Product saved =
                productRepository.save(product);

        return mapToResponse(saved);
    }


    // GET ALL
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // GET BY ID
    public ProductResponse getProductById(
            Long id) {

        validateId(id);

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(product);
    }


    // UPDATE
    public ProductResponse updateProduct(
            Long id,
            UpdateProductRequest request) {

        validateId(id);

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product not found with id: "
                                                + id
                                )
                        );

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setActive(request.getActive());

        Product updated =
                productRepository.save(product);

        return mapToResponse(updated);
    }


    // SOFT DELETE
    public void deleteProduct(Long id) {

        validateId(id);

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product not found with id: "
                                                + id
                                )
                        );

        product.setActive(false);

        productRepository.save(product);
    }


    // ID VALIDATION
    private void validateId(Long id) {

        if (id == null || id <= 0) {

            throw new ProductNotFoundException(
                    "Invalid product id: " + id
            );
        }
    }


    // MAPPER
    private ProductResponse mapToResponse(
            Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.getActive()
        );
    }
}