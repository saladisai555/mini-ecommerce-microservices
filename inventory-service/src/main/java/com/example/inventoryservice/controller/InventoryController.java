package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.CreateInventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.dto.StockQuantityRequest;
import com.example.inventoryservice.service.InventoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(
            InventoryService inventoryService) {

        this.inventoryService = inventoryService;
    }


    // =========================================================
    // CREATE INVENTORY
    // =========================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse createInventory(
            @Valid @RequestBody
            CreateInventoryRequest request) {

        return inventoryService.createInventory(request);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public List<InventoryResponse> getAllInventory() {

        return inventoryService.getAllInventory();
    }


    // =========================================================
    // GET BY PRODUCT ID
    // =========================================================

    @GetMapping("/{productId}")
    public InventoryResponse getInventory(
            @PathVariable
            @Positive(
                    message =
                            "Product ID must be greater than 0"
            )
            Long productId) {

        return inventoryService
                .getInventoryByProductId(productId);
    }


    // =========================================================
    // INCREASE STOCK
    // =========================================================

    @PutMapping("/{productId}/increase")
    public InventoryResponse increaseStock(
            @PathVariable
            @Positive(
                    message =
                            "Product ID must be greater than 0"
            )
            Long productId,

            @Valid @RequestBody
            StockQuantityRequest request) {

        return inventoryService
                .increaseStock(
                        productId,
                        request
                );
    }


    // =========================================================
    // REDUCE STOCK
    // =========================================================

    @PutMapping("/{productId}/reduce")
    public InventoryResponse reduceStock(
            @PathVariable
            @Positive(
                    message =
                            "Product ID must be greater than 0"
            )
            Long productId,

            @Valid @RequestBody
            StockQuantityRequest request) {

        return inventoryService
                .reduceStock(
                        productId,
                        request
                );
    }
}