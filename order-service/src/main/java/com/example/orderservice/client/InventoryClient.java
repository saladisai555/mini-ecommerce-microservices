package com.example.orderservice.client;

import com.example.orderservice.exception.InventoryServiceException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient() {
        this.restClient =
                RestClient.create("http://localhost:8083");
    }

    public void reduceStock(
            Long productId,
            Integer quantity) {
        try {

            restClient
                    .put()
                    .uri(
                            "/api/inventory/{productId}/reduce",
                            productId
                    )
                    .body(
                            new StockQuantityRequest(quantity)
                    )
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            (request, response) -> {
                                throw new InventoryServiceException(
                                        "Unable to reduce stock for product id: "
                                                + productId
                                );
                            }
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            (request, response) -> {
                                throw new InventoryServiceException(
                                        "Inventory Service is unavailable"
                                );
                            }
                    )
                    .toBodilessEntity();

        } catch (InventoryServiceException ex) {

            throw ex;

        } catch (RestClientException ex) {

            throw new InventoryServiceException(
                    "Unable to communicate with Inventory Service"
            );
        }
    }

    private record StockQuantityRequest(Integer quantity) {
    }
}