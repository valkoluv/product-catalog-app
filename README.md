# Product Catalog Application (Spring Boot 3 + JPA + REST/Swagger)

This project demonstrates working with the **Spring Data JPA** framework by implementing a full-featured **Product Catalog**. It includes robust functionality for sorting, pagination, and filtering via both a traditional MVC web interface (Thymeleaf) and a complete **REST API** documented with Swagger (OpenAPI).

The application is built on modern Spring Boot 3 standards and utilizes Jakarta EE specifications.

## 🚀 Key Technologies

  * **Spring Boot 3.5.6**: The application core.
  * **Spring Data JPA**: Persistence layer for database operations.
  * **H2 Database**: In-memory database for rapid development and testing.
  * **Thymeleaf**: Server-side rendering for the MVC web interface.
  * **RESTful API**: Endpoints for managing product resources.
  * **SpringDoc OpenAPI (Swagger)**: Automatic documentation and testing of the REST API.
  * **Java 21**: Programming language.

## 📦 Project Structure and Functionality

The project follows a standard multi-tier architecture:

| Layer | Packages | Responsibility |
| :--- | :--- | :--- |
| **Persistence** | `model`, `repository` | JPA Entities (`Product`, `Category`) and `JpaRepository` interfaces. |
| **Service** | `service` | Contains business logic, handling pagination, sorting, and complex filtering. |
| **Web/MVC** | `controller/ProductController`, `templates` | Controllers for the Thymeleaf web interface. |
| **REST API** | `controller/rest/ProductRestController` | Controllers for JSON interaction. |
| **Utils** | `dto`, `exception`, `config` | Data Transfer Objects (DTOs), custom exception handling, and data initialization. |

-----

## 💻 Web Interface (Thymeleaf)

Access the web catalog at: `http://localhost:8080/products`.

The web interface demonstrates the core capabilities of Spring Data JPA:

  * **Pagination & Sorting**: Displays a list of products with controls for pagination and sorting by name, price, or category name.
  * **Filtering**: Allows filtering products by search term (`searchName`), price range (`minPrice`, `maxPrice`), and specific category (`categoryId`).

-----

## 🌐 REST API Endpoints

The API is fully documented and testable via Swagger UI.

Documentation URL: `http://localhost:8080/swagger-ui/index.html`

| Method | Endpoint | Description | HTTP Statuses |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/v1/products` | Paginated list of products with filtering by price and name. | 200 OK / **400 BAD REQUEST** (if the result page is empty) |
| **GET** | `/api/v1/products/{id}` | Retrieve a single product by ID. | 200 OK / **404 NOT FOUND** |
| **POST** | `/api/v1/products` | Create a new product (ID must be `null`). | 201 CREATED |
| **PUT** | `/api/v1/products/{id}` | Update an existing product. | 200 OK |
| **DELETE** | `/api/v1/products/{id}` | Delete a product by ID. | **204 NO CONTENT** / 404 NOT FOUND |

### Special Status Handling (451)

The `ProductRestController` implements custom exception handling for improper CRUD usage, returning **451 UNAVAILABLE FOR LEGAL REASONS**:

  * Attempting to use **POST to update** an existing resource (i.e., sending a POST request with an existing ID).
  * Attempting to use **PUT to create** a new resource (i.e., sending a PUT request with a `null` ID).

-----

## ▶️ Getting Started

### Prerequisites

  * Java Development Kit (JDK) 21 or newer.
  * Maven 3.9.11 or newer.

### Running the Application

1.  **Clone the repository.**

2.  **Build and run** the application using the Maven wrapper:

    ```bash
    ./mvnw spring-boot:run
    ```

### Verification

  * **Web Catalog**: Access the MVC page at `http://localhost:8080/products`.
  * **Swagger UI**: Test all REST endpoints at `http://localhost:8080/swagger-ui/index.html`.
  * **H2 Console**: Available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:productdb`).

### Data Initialization

The `DataInitializer` class automatically populates the H2 database on startup with sample categories (Electronics, Books, Clothing) and products.
