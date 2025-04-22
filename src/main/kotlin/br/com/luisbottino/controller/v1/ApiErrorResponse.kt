package br.com.luisbottino.controller.v1

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Standard structure for error responses returned by the API.")
data class ApiErrorResponse(
    val timestamp: String = LocalDateTime.now().toString(),
    @Schema(example = "400", description = "HTTP status code of the error.")
    val status: Int,
    @Schema(example = "Bad Request", description = "HTTP status phrase.")
    val error: String,
    @Schema(example = "VAL001", description = "Custom application error code.")
    val code: String,
    @Schema(
        description = "Detailed message(s) about the error.",
        example = "userId: The userId must not be blank or null."
    )
    val message: Any,
    @Schema(example = "/api/v1/conversions", description = "The path of the request that caused the error.")
    val path: String
)
