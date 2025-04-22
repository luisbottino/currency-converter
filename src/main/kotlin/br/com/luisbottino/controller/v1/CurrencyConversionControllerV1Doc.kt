package br.com.luisbottino.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Slice
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Currency Conversion", description = "Endpoints to manage currency conversions operations")
@Validated
interface CurrencyConversionControllerV1Doc {

    @Operation(
        summary = "Retrieve the history of conversions for a user",
        description = "Returns a paginated history of conversions performed by a user based on their user ID.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Conversion performed successfully.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ConversionHistoryResponse::class),
                    examples = [ExampleObject(
                        name = "200-conversion-history",
                        summary = "Conversion history retrieved",
                        value = """
                            {
                              "content": [
                                {
                                  "transactionId": "97",
                                  "userId": "user123",
                                  "fromCurrency": "USD",
                                  "originalAmount": 100.00,
                                  "toCurrency": "BRL",
                                  "convertedAmount": 500.00,
                                  "conversionRate": 5.00,
                                  "timestamp": "2025-10-25T12:00:00"
                                }
                              ],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 10,
                                "sort": {
                                  "sorted": false,
                                  "empty": true,
                                  "unsorted": true
                                },
                                "offset": 0,
                                "paged": true,
                                "unpaged": false
                              },
                              "first": true,
                              "last": true,
                              "size": 10,
                              "number": 0,
                              "sort": {
                                "sorted": false,
                                "empty": true,
                                "unsorted": true
                              },
                              "numberOfElements": 1,
                              "empty": false
                            }
                        """
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request parameters.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiErrorResponse::class),
                    examples = [ExampleObject(
                        name = "400-invalid-request",
                        summary = "Validation failed",
                        value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "code": "VAL001",
                              "message": [
                                "userId: must not be blank",
                                "amount: must be greater than 0"
                              ],
                              "path": "/api/v1/conversions"
                            }
                        """
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User or conversion history not found.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiErrorResponse::class),
                    examples = [ExampleObject(
                        name = "404-not-found",
                        summary = "User or resource not found",
                        value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "code": "RES001",
                              "message": "The requested resource was not found.",
                              "path": "/api/v1/conversions"
                            }
                        """
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiErrorResponse::class),
                    examples = [ExampleObject(
                        name = "500-internal-error",
                        summary = "Unexpected internal error",
                        value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "code": "GEN001",
                              "message": "An unexpected internal error has occurred.",
                              "path": "/api/v1/conversions"
                            }
                        """
                    )]
                )]
            )
        ]
    )
    fun getConversionHistory(
        @Valid request: GetConversionHistoryRequest
    ): Slice<ConversionHistoryResponse>

    @Operation(
        summary = "Convert currency",
        description = "Performs a currency conversion from one currency to another using " +
                "the current exchange rate and returns the conversion details.",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details required to perform the currency conversion",
            required = true,
            content = [Content(schema = Schema(implementation = PostConversionRequest::class))]
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Conversion performed successfully.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ConversionHistoryResponse::class),
                    examples = [ExampleObject(
                        name = "200-conversion-success",
                        summary = "Successful conversion response",
                        value = """
                            {
                              "transactionId": "85",
                              "userId": "luis_bottino",
                              "fromCurrency": "USD",
                              "originalAmount": 70.00,
                              "toCurrency": "BRL",
                              "convertedAmount": 406.57,
                              "conversionRate": 5.81,
                              "timestamp": "2025-04-21T13:32:33.519289"
                            }
                        """
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request parameters.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiErrorResponse::class),
                    examples = [ExampleObject(
                        name = "400-invalid-request",
                        summary = "Validation failed",
                        value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "code": "VAL001",
                              "message": [
                                "userId: must not be blank",
                                "amount: must be greater than 0"
                              ],
                              "path": "/api/v1/conversions"
                            }
                        """
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Invalid or missing API key.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiErrorResponse::class),
                    examples = [ExampleObject(
                        name = "401-invalid-api-key",
                        summary = "API key is missing or invalid",
                        value = """
                            {
                              "status": 401,
                              "error": "Unauthorized",
                              "code": "INF002",
                              "message": "Invalid or missing access key for external API.",
                              "path": "/api/v1/conversions"
                            }
                        """
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User or conversion history not found.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiErrorResponse::class),
                    examples = [ExampleObject(
                        name = "404-not-found",
                        summary = "User or resource not found",
                        value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "code": "RES001",
                              "message": "The requested resource was not found.",
                              "path": "/api/v1/conversions"
                            }
                        """
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "422",
                description = "Business rule violation.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiErrorResponse::class),
                    examples = [ExampleObject(
                        name = "422-business-violation",
                        summary = "Conversion rule violation",
                        value = """
                            {
                              "status": 422,
                              "error": "Unprocessable Entity",
                              "code": "BUS001",
                              "message": "Conversion rate between the requested currencies was not found.",
                              "path": "/api/v1/conversions"
                            }
                        """
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiErrorResponse::class),
                    examples = [ExampleObject(
                        name = "500-internal-error",
                        summary = "Unexpected internal error",
                        value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "code": "GEN001",
                              "message": "An unexpected internal error has occurred.",
                              "path": "/api/v1/conversions"
                            }
                        """
                    )]
                )]
            )
        ]
    )
    fun postConversion(@RequestBody @Valid conversionRequest: PostConversionRequest): ConversionHistoryResponse
}
