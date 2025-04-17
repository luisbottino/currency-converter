# Currency Converter API
**A Currency Converter Service for managing and retrieving historical conversion data.**
### **Stacks**
This project uses the following technologies and tools:
- **Language:** Kotlin
- **Frameworks:**
    - Spring Boot (Spring MVC, Spring Data JPA)
    - Jakarta EE for validation and annotations

- **Persistence:**
    - PostgreSQL (or other relational databases via JPA)

- **API Documentation:**
    - OpenAPI/Swagger (via `springdoc-openapi`)

- **Testing:**
    - JUnit 5 (for unit and integration tests)
    - MockMvc (for Spring MVC integration testing)

- **Build Tool:** Gradle (Kotlin DSL)
- **Other Tools:**
    - Hibernate Validator (for request validation)
    - SLF4J (for logging)

### **Architecture**
The project follows a clean layered architecture:
1. **Controller Layer:**
    - Exposes RESTful endpoints.
    - Validates incoming requests using annotations from Jakarta EE.
    - Delegates business logic to the service layer.

2. **Service Layer:**
    - Contains core business logic.
    - Interfaces with the persistence layer to manage data operations.

3. **Repository Layer:**
    - Uses Spring Data JPA to abstract interaction with the database.
    - Contains repository interfaces with query method definitions.

4. **Domain Models:**
    - Consists of entities and DTOs for representing business data.

5. **Global Exception Handling:**
    - Custom handling for errors such as validation failures (via `MethodArgumentNotValidException`) and other common application exceptions.

### **Conversion History Retrieval (GET /api/v1/conversions)**
This operation retrieves the historical list of conversions performed by a user, paginated based on the parameters provided.
#### **Request**
**Endpoint:**
```
GET /api/v1/conversions
```
**Query Parameters:**
- `userId` (required): The unique identifier of the user whose conversion history is being fetched.
- `page` (optional, default: `0`): The page number for paginated results (starting from 0).
- `size` (optional, default: `10`): The number of items per page.
#### Example Request:
```shell
curl --location 'http://localhost:8080/api/v1/conversions?userId=12345&page=0&size=10'
```
#### **Response**
**Success Response (HTTP 200):**
```json
{
    "content": [
        {
            "id": "conversion-123",
            "userId": "12345",
            "sourceCurrency": "USD",
            "targetCurrency": "EUR",
            "sourceAmount": 100,
            "convertedAmount": 92.5,
            "exchangeRate": 0.925,
            "conversionDate": "2023-10-06T12:00:00"
        },
        {
            "id": "conversion-124",
            "userId": "12345",
            "sourceCurrency": "GBP",
            "targetCurrency": "USD",
            "sourceAmount": 200,
            "convertedAmount": 250,
            "exchangeRate": 1.25,
            "conversionDate": "2023-10-05T10:00:00"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 2,
    "last": true,
    "first": true
}
```
**Error Responses:**
- **400 Bad Request:** Returned when the input parameters are invalid (e.g., missing or invalid `userId`, invalid `page` or `size`). Example:
```json
  {
      "timestamp": "2023-10-06T12:00:00",
      "status": 400,
      "error": "Bad Request",
      "message": [
          "userId: The userId must not be blank or null.",
          "page: The page number must not be negative."
      ],
      "path": "/api/v1/conversions"
  }
```
- **404 Not Found:** Returned if the user has no conversion history.
- **500 Internal Server Error:** Returned for unexpected server-side issues.
### **How to Run**
1. Clone the repository:
```shell
 git clone https://github.com/luisbottino/currency-converter.git
```
2. Build the application:
```shell
   ./gradlew build
```
3. Run the application:
```shell
./gradlew bootRun
```
1. Access the OpenAPI documentation for testing the API:
```
 http://localhost:8080/swagger-ui.html
```
### **Testing**
Run the integration and unit tests using:
```shell
./gradlew test
```
Reports for test results can be found in:
```
/build/reports/tests/test/index.html
```
