# AI_USAGE.md

# AI Usage

As requested in the assessment, this document contains representative prompts used while developing this project.

The project itself was created using **start.spring.io** before any AI assistance was used and then imported into IntelliJ IDEA.

AI was used as a development assistant to explore implementation approaches, understand framework features, discuss architectural trade-offs and accelerate repetitive implementation tasks where appropriate.

All design decisions, implementation, debugging, testing, integration and final verification were completed by me.

The prompts below are representative of how AI was used during development.

---

# Representative Prompts

## Prompt 1 — Dynamic Filtering

```text
I'm implementing a product catalogue in Spring Boot.

The listing endpoint should support pagination, sorting and optional filtering by name and category.

Would JpaSpecificationExecutor be a good fit here?

Please explain the trade-offs compared to writing multiple repository methods and show an example implementation following Spring Boot best practices.
```

---

## Prompt 2 — Transactions

```text
I'm implementing order placement in Spring Boot.

The flow validates stock, deducts inventory and creates an order.

Should these operations be wrapped in a single transaction?

Explain why and discuss what could go wrong if they weren't transactional.
```

---

## Prompt 3 — Reporting

```text
I'm implementing a reporting endpoint that returns the top-selling products between two dates.

The query joins several tables, groups data and calculates totals.

Would you recommend Spring Data JPA or JdbcTemplate for this use case?

Explain the trade-offs and show an example implementation using the approach you recommend.
```

---

## Prompt 4 — Pre-submission Review

```text
I'm almost finished with a Spring Boot assessment.

The application uses:

- Spring Boot 3
- Java 17
- Spring Data JPA
- JdbcTemplate
- Spring Security
- Docker

What are some common implementation mistakes or areas I should double-check before submitting?

Focus on Spring Boot best practices, maintainability and code quality rather than introducing additional frameworks.
```

---

## Prompt 5 — Data Access Approaches

```text
Explain the trade-offs between:

- JpaSpecificationExecutor
- QueryDSL
- jOOQ

When would you choose each approach?

Use practical Spring Boot examples and discuss how complexity, maintainability and SQL requirements influence the decision.
```

---

# Summary

AI was used to discuss implementation approaches, understand framework features and validate design decisions during development.

The final implementation, testing and submission were completed after reviewing and integrating the generated suggestions.
