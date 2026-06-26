# SOLUTION.md

# Solution Overview

The goal of this assessment was to build a simple ecommerce REST API using Spring Boot while demonstrating practical backend engineering skills.

Rather than trying to build a large ecommerce platform, I focused on delivering a clean vertical slice that satisfies the requirements while keeping the code easy to understand and maintain.

The application supports:

* Product catalogue management
* Order placement
* Order cancellation
* Inventory management
* Sales reporting

I also completed the optional requirements by including:

* Swagger / OpenAPI
* Basic Authentication
* Docker
* GitHub Actions

As a small enhancement, I added Spring Boot Actuator to expose a standard health endpoint.

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

Each layer has a single responsibility.

### Controllers

Controllers expose the REST API.

They receive requests, validate input and delegate business logic to the service layer.

### Services

Services contain the business rules.

This includes:

* placing orders
* cancelling orders
* stock validation
* inventory updates

Transaction boundaries are also defined in this layer.

### Repositories

Repositories are responsible for persistence.

Spring Data JPA is used for transactional CRUD operations.

JdbcTemplate is used for reporting.

Keeping these responsibilities separate makes the application easier to understand, maintain and test.

---

# Domain Model

The application contains three primary domain entities.

## Product

Represents a sellable item.

Stores:

* name
* category
* price
* stock quantity

---

## CustomerOrder

Represents a customer's order.

Stores:

* creation date
* order status
* collection of order items

---

## OrderItem

Represents an individual product purchased within an order.

Each order item stores:

* product
* quantity
* purchase price

The purchase price is stored intentionally.

If the product price changes in the future, historical orders should still reflect the amount originally paid.

---

# Product Catalogue

Products can be:

* created
* retrieved individually
* listed

The list endpoint supports:

* pagination
* sorting
* optional filtering by name
* optional filtering by category

## Why Pageable?

Spring Data already provides a well-tested pagination abstraction.

Using Pageable avoids writing custom pagination logic while remaining familiar to most Spring Boot developers.

## Why JpaSpecificationExecutor?

I considered writing separate repository methods for each filter combination.

For example:

```text
findByName(...)
findByCategory(...)
findByNameAndCategory(...)
```

That approach becomes difficult to maintain as more optional filters are introduced.

Instead, I used JpaSpecificationExecutor so the query is built dynamically based on whichever filters the client actually supplies.

This keeps the repository small while allowing additional filters to be added easily in the future.

---

# Validation

Validation follows business rules rather than arbitrary limits.

Products require:

* name
* category
* positive price

Stock is allowed to be zero because products can temporarily be unavailable.

Negative stock is not allowed.

Orders must contain at least one item.

Quantities must be greater than zero.

I intentionally avoided maximum values because the assessment did not define business limits.

---

# Order Processing

The order workflow is intentionally straightforward.

When an order is placed the application:

1. Finds each requested product.
2. Verifies sufficient stock exists.
3. Reduces inventory.
4. Creates the order.
5. Stores each order item.
6. Returns the completed order.

---

# Transactions

Both order placement and cancellation are transactional.

This ensures inventory and order data always remain consistent.

If stock validation fails, the transaction rolls back and no order is created.

Likewise, cancelling an order restores inventory and updates the order status within the same transaction.

---

# Reporting

The reporting endpoint returns the top-selling products within a date range.

For this endpoint I intentionally chose JdbcTemplate instead of JPA.

The query:

* joins products
* joins customer orders
* joins order items
* filters confirmed orders
* groups by product
* calculates quantity sold
* calculates revenue
* orders by quantity sold
* limits the result set

This is fundamentally a reporting query rather than a CRUD operation.

While JPA could be used, SQL expresses this type of aggregate query much more naturally.

Using JdbcTemplate also demonstrates SQL proficiency, which was one of the assessment requirements.

---

# Security

The optional requirement requested Basic Authentication.

For that reason I chose Spring Security's HTTP Basic support.

Only write operations require authentication.

Read operations remain public.

This keeps the solution aligned with the assessment while avoiding the additional complexity of JWT.

For demonstration purposes I used an in-memory administrator account.

The project intentionally uses the `noop` password encoder to keep the credentials simple for reviewers.

In a production system I would instead use BCrypt or Argon2 together with externalised secrets and HTTPS.

---

# Docker

Docker support was included from the beginning.

This allows the reviewer to clone the repository and start both PostgreSQL and the application with a single command.

```bash
docker compose up --build
```

No local PostgreSQL installation is required.

---

# GitHub Actions

A simple CI workflow is included.

Each push and pull request automatically:

* checks out the source
* installs Java 17
* executes

```bash
./gradlew clean test
```

This provides quick feedback that the application still builds successfully and that the tests continue to pass.

---

# Testing

The included tests focus on business behaviour rather than simply increasing code coverage.

The scenarios covered include:

* successful order placement
* insufficient stock
* inventory reduction
* order totals

These tests validate the most important business rules in the application.

---

# Actuator Enhancement

Although not required, I added Spring Boot Actuator.

This exposes:

```text
/actuator/health
```

It provides a lightweight health endpoint and is commonly included in production Spring Boot services.

---

# Future Improvements

If this project evolved into a production service I would introduce:

* Flyway database migrations
* Testcontainers
* BCrypt or Argon2 password encoding
* Externalised configuration and secrets
* OAuth2 or OpenID Connect
* Micrometer and Prometheus metrics
* SQL indexing based on production query patterns
* Execution plan analysis
* Database views for more complex reporting
* QueryDSL or jOOQ if query complexity increased

---

# Closing Thoughts

For this assessment I deliberately chose simple, well-understood Spring Boot patterns.

Where Spring Data JPA was a good fit, I used it.

Where SQL provided a clearer solution, I used JdbcTemplate.

The goal was not to introduce every available framework, but to choose the right tool for each part of the application while keeping the overall design maintainable and easy to reason about.
