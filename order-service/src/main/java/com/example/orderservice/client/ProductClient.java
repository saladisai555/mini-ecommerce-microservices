package com.example.orderservice.client;

import com.example.orderservice.dto.ProductResponse;
import com.example.orderservice.exception.ProductNotFoundException;
import com.example.orderservice.exception.ProductInactiveException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient() {
        this.restClient =
                RestClient.create("http://localhost:8081");
    }

    public ProductResponse getProductById(Long productId) {

        try {

            return restClient
                    .get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 404,
                            (request, response) -> {
                                throw new ProductNotFoundException(
                                        "Product not found with id: "
                                                + productId
                                );
                            }
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            (request, response) -> {
                                throw new RuntimeException(
                                        "Product Service is unavailable"
                                );
                            }
                    )
                    .body(ProductResponse.class);

        } catch (ProductNotFoundException ex) {

            throw ex;

        } catch (RestClientException ex) {

            throw new RuntimeException(
                    "Unable to communicate with Product Service"
            );
        }
    }
}