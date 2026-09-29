package com.example.inventoryservice.service;

import com.example.inventoryservice.client.ProductClient;
import com.example.inventoryservice.dto.CreateInventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.dto.ProductResponse;
import com.example.inventoryservice.dto.StockQuantityRequest;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.exception.InsufficientStockException;
import com.example.inventoryservice.exception.InventoryAlreadyExistsException;
import com.example.inventoryservice.exception.InventoryNotFoundException;
import com.example.inventoryservice.exception.ProductInactiveException;
import com.example.inventoryservice.repository.InventoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ProductClient productClient;

    public InventoryService(
            InventoryRepository inventoryRepository,
            ProductClient productClient) {

        this.inventoryRepository = inventoryRepository;
        this.productClient = productClient;
    }


    // =========================================================
    // CREATE INVENTORY
    // =========================================================

    @Transactional
    public InventoryResponse createInventory(
            CreateInventoryRequest request) {

        Long productId = request.getProductId();

        // -----------------------------------------------------
        // 1. Verify product exists
        // -----------------------------------------------------

        ProductResponse product =
                productClient.getProductById(productId);


        // -----------------------------------------------------
        // 2. Product must be active
        // -----------------------------------------------------

        validateProductIsActive(product);


        // -----------------------------------------------------
        // 3. Prevent duplicate inventory
        // -----------------------------------------------------

        if (inventoryRepository.existsByProductId(productId)) {

            throw new InventoryAlreadyExistsException(
                    "Inventory already exists for product id: "
                            + productId
            );
        }


        // -----------------------------------------------------
        // 4. Create inventory
        // -----------------------------------------------------

        Inventory inventory = new Inventory();

        inventory.setProductId(productId);

        inventory.setAvailableQuantity(
                request.getAvailableQuantity()
        );


        Inventory saved =
                inventoryRepository.save(inventory);


        return mapToResponse(saved);
    }


    // =========================================================
    // GET ALL INVENTORY
    // =========================================================

    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET INVENTORY BY PRODUCT ID
    // =========================================================

    public InventoryResponse getInventoryByProductId(
            Long productId) {

        // Verify that product exists
        productClient.getProductById(productId);


        Inventory inventory =
                inventoryRepository
                        .findByProductId(productId)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(
                                        "Inventory not found for product id: "
                                                + productId
                                )
                        );


        return mapToResponse(inventory);
    }


    // =========================================================
    // INCREASE STOCK
    // =========================================================

    @Transactional
    public InventoryResponse increaseStock(
            Long productId,
            StockQuantityRequest request) {

        // -----------------------------------------------------
        // 1. Verify product
        // -----------------------------------------------------

        ProductResponse product =
                productClient.getProductById(productId);


        // -----------------------------------------------------
        // 2. Product must be active
        // -----------------------------------------------------

        validateProductIsActive(product);


        // -----------------------------------------------------
        // 3. Find existing inventory
        // -----------------------------------------------------

        Inventory inventory =
                inventoryRepository
                        .findByProductId(productId)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(
                                        "Inventory not found for product id: "
                                                + productId
                                )
                        );


        // -----------------------------------------------------
        // 4. Increase stock
        // -----------------------------------------------------

        int newQuantity =
                inventory.getAvailableQuantity()
                        + request.getQuantity();


        inventory.setAvailableQuantity(newQuantity);


        Inventory saved =
                inventoryRepository.save(inventory);


        return mapToResponse(saved);
    }


    // =========================================================
    // REDUCE STOCK
    // =========================================================

    @Transactional
    public InventoryResponse reduceStock(
            Long productId,
            StockQuantityRequest request) {

        // -----------------------------------------------------
        // 1. Verify product
        // -----------------------------------------------------

        ProductResponse product =
                productClient.getProductById(productId);


        // -----------------------------------------------------
        // 2. Product must be active
        // -----------------------------------------------------

        validateProductIsActive(product);


        // -----------------------------------------------------
        // 3. Find inventory
        // -----------------------------------------------------

        Inventory inventory =
                inventoryRepository
                        .findByProductId(productId)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(
                                        "Inventory not found for product id: "
                                                + productId
                                )
                        );


        // -----------------------------------------------------
        // 4. Check sufficient stock
        // -----------------------------------------------------

        if (inventory.getAvailableQuantity()
                < request.getQuantity()) {

            throw new InsufficientStockException(
                    "Insufficient stock for product id: "
                            + productId
                            + ". Available: "
                            + inventory.getAvailableQuantity()
                            + ", Requested: "
                            + request.getQuantity()
            );
        }


        // -----------------------------------------------------
        // 5. Reduce stock
        // -----------------------------------------------------

        int newQuantity =
                inventory.getAvailableQuantity()
                        - request.getQuantity();


        inventory.setAvailableQuantity(newQuantity);


        Inventory saved =
                inventoryRepository.save(inventory);


        return mapToResponse(saved);
    }


    // =========================================================
    // PRODUCT ACTIVE VALIDATION
    // =========================================================

    private void validateProductIsActive(
            ProductResponse product) {

        if (!Boolean.TRUE.equals(product.getActive())) {

            throw new ProductInactiveException(
                    "Product is inactive: "
                            + product.getId()
            );
        }
    }


    // =========================================================
    // MAPPER
    // =========================================================

    private InventoryResponse mapToResponse(
            Inventory inventory) {

        return new InventoryResponse(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getAvailableQuantity()
        );
    }
}