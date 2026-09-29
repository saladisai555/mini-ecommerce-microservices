# Mini E-Commerce Microservices System

A learning-focused microservices project built using Java and Spring Boot.

## Architecture

The system contains three independent microservices:

- Product Service
- Order Service
- Inventory Service

Each service owns its own database.

```text
                    Product Service
                         :8081
                           ▲
                           │
                       RestClient
                           │
                    Order Service
                         :8082
                           │
                       RestClient
                           │
                           ▼
                   Inventory Service
                         :8083

Technologies
- Java 21
- Spring Boot
- Spring Data JPA
- Microsoft SQL Server
- REST APIs
- RestClient
- Maven
Services
Product Service
Port: 8081
Database: PRODUCT_DB
Responsibilities:
- Product creation
- Product retrieval
- Product update
- Product deactivation
Order Service
Port: 8082
Database: ORDER_DB
Responsibilities:
- Order creation
- Order retrieval
- Product validation
- Order price calculation
- Inventory coordination
Inventory Service
Port: 8083
Database: INVENTORY_DB
Responsibilities:
- Inventory creation
- Stock retrieval
- Increase stock
- Reduce stock
- Stock validation

                         
