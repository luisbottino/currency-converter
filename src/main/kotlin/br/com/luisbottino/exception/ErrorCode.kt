package br.com.luisbottino.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val description: String,
    val httpStatus: HttpStatus
) {
    VALIDATION_ERROR(
        code = "VAL001",
        description = "Validation error: one or more fields are invalid.",
        httpStatus = HttpStatus.BAD_REQUEST
    ),

    CURRENCY_RATE_NOT_FOUND(
        code = "BUS001",
        description = "Conversion rate between the requested currencies was not found.",
        httpStatus = HttpStatus.UNPROCESSABLE_ENTITY
    ),

    EXTERNAL_SERVICE_UNAVAILABLE(
        code = "INF001",
        description = "Failed to retrieve data from an external service.",
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    ),

    INVALID_API_KEY(
        code = "INF002",
        description = "Invalid or missing access key for external API.",
        httpStatus = HttpStatus.UNAUTHORIZED
    ),

    UNKNOWN_ERROR(
        code = "GEN001",
        description = "An unexpected internal error has occurred.",
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    ),

}
