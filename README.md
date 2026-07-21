# Library Management REST API

## Overview

This project implements a RESTful API for a simple library management system.

The API allows users to:

* Register borrowers
* Register books
* View available books
* Borrow books on behalf of borrowers
* Return borrowed books

The application is built using:

* Java 17
* Spring Boot 3.5
* Spring Data JPA
* PostgreSQL
* Docker Compose

---

# Technology Stack

| Technology        | Purpose                             |
| ----------------- | ----------------------------------- |
| Java 17           | Programming language                |
| Spring Boot       | REST API framework                  |
| Spring Data JPA   | Database interaction                |
| PostgreSQL        | Relational database                 |
| Maven             | Dependency management               |
| Docker Compose    | Containerized application execution |
| OpenAPI / Swagger | API documentation                   |

---

# Database Choice

## PostgreSQL

PostgreSQL was selected because:

* The system has clearly defined relationships between entities:

    * Borrower
    * Book
    * BookBorrow
* Relational databases provide strong consistency for borrowing operations.
* Foreign key constraints ensure data integrity.
* Unique constraints can enforce business rules such as preventing duplicate active borrowing records.
* PostgreSQL partial indexes provide an efficient way to enforce conditional uniqueness rules.
* PostgreSQL is widely used in production environments and has excellent Spring Boot support.

---

# Project Structure

```
library-application
|
├── src/main/java
│   └── com.collabera.library_application
│
├── src/main/resources
│   ├── application.properties
│   ├── application-dev.properties
│   └── application-prod.properties
│
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

# Running the Application

## Prerequisites

Install:

* Java 17
* Maven
* Docker Desktop

---

# Option 1: Run Locally

## Configure Development Database

The development profile uses a local PostgreSQL instance.

Example:

`application-dev.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/library_db
spring.datasource.username=postgres
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
```

Start the application:

```bash
mvn spring-boot:run
```

The API will be available at:

```
http://localhost:8080
```

---

# Option 2: Run with Docker Compose

The Docker setup starts:

* PostgreSQL database
* Spring Boot application

Build and start:

```bash
mvn clean package

docker compose up --build
```

The API will be available at:

```
http://localhost:8080
```

Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

Stop containers:

```bash
docker compose down
```

Remove database volume:

```bash
docker compose down -v
```

---

# Environment Configuration

The application supports multiple environments using Spring Profiles.

Available profiles:

| Profile | Purpose                            |
| ------- | ---------------------------------- |
| dev     | Local development                  |
| prod    | Docker/production-like environment |

Activate a profile:

```bash
SPRING_PROFILES_ACTIVE=prod
```

Production database configuration is provided using environment variables:

```
DB_URL
DB_USERNAME
DB_PASSWORD
```

Example:

```
DB_URL=jdbc:postgresql://postgres:5432/library_db
DB_USERNAME=postgres
DB_PASSWORD=postgres123
```

---

# API Endpoints

## Borrower APIs

### Register Borrower

```
POST /api/library/borrowers
```

Request:

```json
{
  "name": "John Smith",
  "email": "john@example.com"
}
```

Response:

```json
{
  "id": 1,
  "name": "John Smith",
  "email": "john@example.com"
}
```

---

# Book APIs

## Register Book

```
POST /api/library/books
```

Request:

```json
{
  "isbn": "9780134685991",
  "title": "Effective Java",
  "author": "Joshua Bloch"
}
```

Response:

```json
{
  "id": 1,
  "isbn": "9780134685991",
  "title": "Effective Java",
  "author": "Joshua Bloch"
}
```

---

## Get All Books

```
GET /api/library/books
```

Response:

```json
[
  {
    "id": 1,
    "isbn": "9780134685991",
    "title": "Effective Java",
    "author": "Joshua Bloch"
  }
]
```

---

# Book Borrow APIs

## Borrow a Book

```
POST /api/library/books/borrow
```

Request:

```json
{
  "bookId": 1,
  "borrowerId": 2
}
```

Rules:

* A book copy can only have one active borrowing record at a time.
* An active borrowing record is identified by `returned_at IS NULL`.
* Multiple copies with the same ISBN are supported.
* A returned book can be borrowed again, creating a new borrowing transaction.

---

## Return a Book

```
POST /api/library/books/return
```

Example:

```json
{
    "bookId":1
}
```

---

# Validation Rules

## Borrower

* Name is mandatory.
* Email is mandatory.
* Email must be unique.

## Book

* Title is mandatory.
* Author is mandatory.
* ISBN is mandatory.
* ISBN uniqueness is not enforced because multiple physical copies of the same edition are supported.
* Different ISBN values represent different book editions.

---

# Data Model

## Borrower

```
Borrower
---------
id
name
email
created_at
updated_at
```

---

## Book

```
Book
---------
id
isbn
title
author
created_at
updated_at
```

---

## BookBorrow

```
BookBorrow
------------
id
borrower_id
book_id
borrowed_at
returned_at
```

A BookBorrow record represents a borrowing transaction.

## Active Borrow Constraint

To prevent the same book copy from being borrowed by multiple borrowers at the same time, PostgreSQL enforces a partial unique index:

```sql
CREATE UNIQUE INDEX uk_active_book_borrow
ON book_borrows(book_id)
WHERE returned_at IS NULL;
```

Example:

```
Allowed: 

book_id | returned_at
--------|------------
1       | 2026-07-01
1       | 2026-07-15
1       | NULL
---

Not Allowed: 
book_id | returned_at
--------|------------
1       | NULL
1       | NULL

```

# Assumptions

The following assumptions were made because they were not explicitly specified:

1. ISBN uniqueness

   The requirement states that multiple copies with the same ISBN are allowed. Therefore, ISBN is not used as the database primary key.

2. Book identity

   Each physical copy of a book has its own unique ID.

   Example:

   ```
   ID  ISBN          Title
   1   1234567890    Clean Code
   2   1234567890    Clean Code
   ```

   These represent two copies of the same book.

3. Borrowing rules

   A book copy cannot have more than one active borrowing record.
   This rule is enforced at two levels:

    - Application level: The service checks whether an active borrowing record exists.
    - Database level: PostgreSQL partial unique index prevents duplicate active borrowing records during concurrent requests.

4. Returning books

   Returning a book updates the active borrowing record with a return date.

5. Authentication

   Authentication and authorization are not implemented because they were not part of the requirements.

---

# Error Handling

The API returns structured error responses with HTTP status code.

Example:

```json
{
  "status": "ERROR",
  "message": "Borrower email already exists"
}
```
HTTP status codes used:404 Not Found, 409 Conflict, 400 Bad Request, 500 Internal Server Error
---

# Testing

Unit tests are implemented using JUnit 5 and Mockito.

The main business scenarios covered are:

## Borrower Registration

| Scenario                                    | Expected Result                  |
| ------------------------------------------- | -------------------------------- |
| Register borrower with valid name and email | Borrower is created successfully |
| Register borrower with existing email       | Returns conflict error           |
| Register borrower with missing name         | Returns validation error         |
| Register borrower with invalid email format | Returns validation error         |

---

## Book Registration

| Scenario                                         | Expected Result                                      |
| ------------------------------------------------ | ---------------------------------------------------- |
| Register book with valid ISBN, title, and author | Book is created successfully                         |
| Register book with missing title                 | Returns validation error                             |
| Register book with missing ISBN                  | Returns validation error                             |
| Register multiple copies with same ISBN          | Multiple book records are created with different IDs |

---

## Borrowing a Book

| Scenario                               | Expected Result                                |
| -------------------------------------- |------------------------------------------------|
| Borrow an available book               | Creates a BookBorrow record successfully       |
| Borrow a book that is already borrowed | Returns `BOOK_ALREADY_BORROWED` conflict error |
| Borrow a non-existing book             | Returns not found error                        |
| Borrow using a non-existing borrower   | Returns not found error                        |

---

## Returning a Book

| Scenario                            | Expected Result                  |
| ----------------------------------- | -------------------------------- |
| Return an active borrowed book      | Updates return date successfully |
| Return a book that was not borrowed | Returns error                    |
| Return a non-existing book          | Returns not found error          |

---

## Data Integrity Tests

| Scenario                                                | Expected Result                    |
| ------------------------------------------------------- | ---------------------------------- |
| Same ISBN with different titles/authors                 | Validation/business rule violation |
| Same ISBN, title, and author                            | Multiple copies are allowed        |
| Same book copy borrowed by two borrowers simultaneously | Second borrow request fails        |

---

Run tests using:

```bash
mvn test
```


---

# Architecture

The application follows a layered architecture based on Spring Boot best practices.

```
                    Client
                      |
                      |
              REST API Controllers
                      |
                      |
              Application Services
                      |
                      |
              Repository Layer
                      |
                      |
                PostgreSQL Database
```

## Components

### Controller Layer

Responsible for:

* Exposing RESTful API endpoints.
* Handling HTTP requests and responses.
* Performing request validation.
* Returning appropriate HTTP status codes.

Examples:

* BorrowerController
* BookController
* BookBorrowController

### Service Layer

Responsible for:

* Implementing business logic.
* Managing borrowing and returning rules.
* Coordinating between entities and repositories.
* Handling transactional operations.

Examples:

* BorrowerService
* BookService
* BookBorrowService

### Repository Layer

Responsible for:

* Database communication.
* CRUD operations using Spring Data JPA.
* Querying borrower, book, and borrowing records.

The borrowing repository provides optimized existence checks:

```java
existsByBookIdAndReturnedAtIsNull(Long bookId)
```


## Database Layer

PostgreSQL stores:

* Borrower information.
* Book information.
* Borrowing history.

Entity relationships:

```
Borrower
    |
    | 1..*
    |
BookBorrow
    |
    | *..1
    |
Book
```

A `BookBorrow` entity is used to represent a borrowing transaction between a borrower and a book copy.

---

## Design Decisions

### Why a layered monolithic architecture?

The library system has a small and well-defined domain. A monolithic architecture provides:

* Simpler deployment.
* Easier maintenance.
* Lower operational complexity.
* Clear separation of responsibilities.

The application can be evolved into separate services in the future if business requirements grow.

---

## Transaction Handling

Borrowing and returning operations are executed within database transactions to maintain data consistency database transactions using Spring's `@Transactional`

### Borrowing transaction flow:

1. Validate that the book exists.
2. Validate that the borrower exists.
3. Check whether the book has an active borrowing record.
4. Create a new borrowing transaction.

If any operation fails, the transaction is rolled back.

### Concurrency Handling

The application uses a two-layer approach:

1. Application-level validation:
    - Checks availability before creating a borrow record.
    - Provides meaningful business errors.

2. Database-level protection:
    - PostgreSQL partial unique index prevents multiple active borrowing records for the same book copy.

This ensures data consistency even when multiple users attempt to borrow the same book simultaneously.

---

## Configuration Management

The application supports multiple environments using Spring Profiles:

* `dev` profile for local development.
* `prod` profile for Docker/production-like execution.

Database credentials are externalized using environment variables.

## API Testing with Postman

The Postman collection is available here:

`postman/book-library-api.postman_collection.json`

### Import Instructions

1. Open Postman
2. Click Import
3. Select the collection JSON file
4. Run the API requests

# Future Improvements

Possible enhancements:

* Add authentication using JWT
* Add pagination for books API
* Add CI/CD pipeline
* Deploy using Kubernetes
* Add integration tests using Testcontainers
