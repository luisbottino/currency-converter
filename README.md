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
- **404 Not Found:** Returned if the resource was not found.
- **500 Internal Server Error:** Returned for unexpected server-side issues.
### **Currency Conversion (POST /api/v1/conversions)**
This operation performs a currency conversion using the current exchange rate and stores the result in the database.
#### **Endpoint:**
```
POST /api/v1/conversions
```
#### **Request Body:**
```json
{
  "userId": "12345",
  "fromCurrency": "USD",
  "toCurrency": "BRL",
  "amount": 100.00
}
```
**Request Fields:**
- `userId` (required): The unique identifier of the user whose conversion history is being fetched.
- `fromCurrency` (required): The source currency code (e.g., USD).
- `toCurrency ` (required): The target currency code (e.g., BRL).
- `amount` (required): The numeric value to convert. Must be a positive decimal.
#### Example Request:
```shell
    curl --location 'http://localhost:8080/api/v1/conversions' \
    --header 'Content-Type: application/json' \
    --data '{
      "userId": "12345",
      "fromCurrency": "USD",
      "toCurrency": "BRL",
      "amount": 100.00
    }'
```
#### **Response**
**Success Response (HTTP 201):**
```json
{
  "transactionId": "1",
  "userId": "12345",
  "fromCurrency": "USD",
  "originalAmount": 100.00,
  "toCurrency": "BRL",
  "convertedAmount": 500.00,
  "conversionRate": 5.0,
  "timestamp": "2025-04-22T12:00:00"
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
        "amount: The amount must be greater than or equal to 0."
      ],
      "path": "/api/v1/conversions"
  }
```
- **401 Unauthorized:** Invalid API key from the external exchange rate service.
- **422 Unprocessable Entity:** Missing exchange rate for the given currency pair. Example:
```json
  {
  "status": 422,
  "error": "Unprocessable Entity",
  "code": "BUS001",
  "message": "Conversion rate between USD and BRL was not found.",
  "path": "/api/v1/conversions"
}
```
- **500 Internal Server Error:** Returned for unexpected server-side issues.

### **How to Run**
1. Clone the repository:
```shell
  git clone https://github.com/luisbottino/currency-converter.git
```
2. Start the WireMock container (used to mock the external exchange API):

The application uses a mock server to simulate responses from the real currency exchange API. This is useful because the real API (https://api.exchangeratesapi.io) has rate limits that may interfere with local testing.

To start the mock server, run:
```shell
    docker run -d \
      -p 8081:8080 \
      -v $PWD/wiremock:/home/wiremock \
      wiremock/wiremock:latest
```
This will mount the local wiremock/ directory (which contains mock mappings and responses) into the container.
```yml
apis:
  api-exchange:
    base-url: http://localhost:8081
```
3. (Optional) To use the real external API instead of the mock:

Edit the application.yml file and change the property:
```yml
apis:
  api-exchange:
    base-url: https://api.exchangeratesapi.io
```
>⚠️ The real API has usage limits on free tiers. Use it with caution to avoid disruptions.

4. Build the application:
```shell
  ./gradlew build
```
5. Run the application:
```shell
  ./gradlew bootRun
```
6. Access the OpenAPI documentation for testing the API:
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
