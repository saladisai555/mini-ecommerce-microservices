package com.example.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;

public class UpdateStockRequest {

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    public UpdateStockRequest() {
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}