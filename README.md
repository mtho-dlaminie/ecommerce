# README.md

# Pick n Pay Backend Assessment

A Spring Boot 3 REST API that simulates a simple ecommerce platform. The application supports product management, order processing, inventory updates and sales reporting while following a clean layered architecture.

The goal of this project was to build a simple, maintainable solution that satisfies the assessment requirements without unnecessary complexity, while still demonstrating good engineering practices and clear design decisions.

---

# Technology Stack

* Java 17
* Spring Boot 3
* Spring MVC
* Spring Data JPA
* PostgreSQL
* H2 Database
* Bean Validation
* Spring Security (Basic Authentication)
* Springdoc OpenAPI (Swagger)
* Docker & Docker Compose
* GitHub Actions
* Spring Boot Actuator *(additional enhancement)*

---

# Architecture

The project follows a traditional layered architecture.

```text
Controller
      ↓
Service
      ↓
Repository
      ↓
Database
```

Responsibilities are clearly separated:

* **Controllers** expose the REST API and handle request validation.
* **Services** contain the business logic and transaction boundaries.
* **Repositories** handle persistence.
* **Entities** represent the domain model.

This structure keeps the project simple, testable and easy to extend.

---

# Features

## Product Catalogue

* Create products
* Retrieve a single product
* List products
* Pagination
* Sorting
* Optional filtering by:

    * Name
    * Category

The listing endpoint uses Spring Data's `Pageable` together with `JpaSpecificationExecutor` so that filters are built dynamically only when they are supplied.

---

## Order Management

* Place an order
* Retrieve an order
* Cancel an order

Business rules include:

* Product existence validation
* Stock validation
* Inventory deduction
* Inventory restoration when cancelling an order

Order placement and cancellation are transactional to ensure inventory always remains consistent.

---

## Sales Reporting

The reporting endpoint returns the top-selling products for a given period.

Unlike the CRUD endpoints, this report is implemented using **JdbcTemplate** and plain SQL.

This was an intentional design decision because the query joins multiple tables, groups data, calculates aggregates and orders the results.

For this type of reporting query, SQL is often easier to read, maintain and optimise than forcing the same logic through JPA.

---

# Security

The assessment requested **Basic Authentication** as an optional enhancement.

Only write operations require authentication.

Protected endpoints:

```text
POST /api/products
POST /api/orders
POST /api/orders/{id}/cancel
```

Read operations remain public.

For demonstration purposes, an in-memory administrator is configured.

```text
Username: admin
Password: admin123
```

In a production system this would be replaced with properly encoded passwords, externalised secrets and a real identity provider.

---

# API Documentation

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

Swagger documents every endpoint and provides an easy way to test the API directly from the browser.

---

# Health Endpoint

As an additional enhancement, Spring Boot Actuator has been included.

Health endpoint:

```text
http://localhost:8080/actuator/health
```

This was not required by the assessment but demonstrates a small operational readiness improvement.

---

# Running the Application

## Local Development

```bash
./gradlew bootRun
```

The application starts using the embedded H2 database.

---

## Docker

Run the application together with PostgreSQL using Docker Compose.

```bash
docker compose up --build
```

Docker starts:

* PostgreSQL
* Spring Boot application

No manual database installation is required.

---

# Running Tests

```bash
./gradlew clean test
```

The tests cover key business scenarios including:

* Successful order placement
* Inventory reduction
* Insufficient stock
* Order totals

The focus is on validating business behaviour rather than simply increasing code coverage.

---

# Continuous Integration

A simple GitHub Actions workflow is included.

On every push and pull request it:

* Checks out the source code
* Sets up Java 17
* Executes

```bash
./gradlew clean test
```

This provides quick feedback that the project still builds successfully and all tests pass.

---

# Example Requests

## Create Product

```bash
curl -u admin:admin123 \
-H "Content-Type: application/json" \
-d '{
  "name":"Rice 10kg",
  "category":"Groceries",
  "price":169.99,
  "stockQuantity":15
}' \
http://localhost:8080/api/products
```

---

## List Products

```bash
curl "http://localhost:8080/api/products?page=0&size=20&sort=price,desc"
```

---

## Place Order

```bash
curl -u admin:admin123 \
-H "Content-Type: application/json" \
-d '{
  "items":[
      {
        "productId":1,
        "quantity":2
      },
      {
        "productId":2,
        "quantity":1
      }
  ]
}' \
http://localhost:8080/api/orders
```

---

## Retrieve Order

```bash
curl http://localhost:8080/api/orders/1
```

---

## Cancel Order

```bash
curl -u admin:admin123 \
-X POST \
http://localhost:8080/api/orders/1/cancel
```

---

## Top Selling Products Report

```bash
curl "http://localhost:8080/api/reports/top-selling-products?from=2026-01-01T00:00:00&to=2027-01-01T00:00:00&limit=5"
```

---

# Project Documentation

The repository also contains:

* **SOLUTION.md** – explains the architecture, design decisions and implementation trade-offs.
* **AI_USAGE.md** – documents how AI was used during development together with representative prompts.

---

# Future Improvements

If this project evolved beyond the assessment, I would consider adding:

* Flyway database migrations
* Testcontainers
* BCrypt or Argon2 password encoding
* OAuth2 / OpenID Connect
* Externalised secrets
* Micrometer & Prometheus metrics
* SQL indexing and execution plan optimisation
* Database views for complex reports
* QueryDSL or jOOQ for more advanced query requirements

---

Thank you for taking the time to review this submission.
