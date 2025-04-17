package br.com.luisbottino.controller.v1

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class GetConversionHistoryRequest(
    @field:NotNull(message = "The userId must not be blank or null.")
    @field:NotBlank(message = "The userId must not be blank or null.")
    @Schema(description = "The user ID whose conversion history is to be fetched",
        example = "12345",
        required = true)
    val userId: String,

    @field:Min(value = 0, message = "The page number must not be negative.")
    @Schema(
        description = "Page number for pagination (starting from 0)",
        example = "0",
        minimum = "0",
        defaultValue = "0")
    val page: Int = 0,

    @field:Min(value = 1, message = "The page size must be at least 1.")
    @Schema(
        description = "Page size specifying the maximum number of items per page",
        example = "10",
        minimum = "1",
        defaultValue = "10")
    val size: Int = 10

)
