package br.com.luisbottino.controller.v1

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class PostConversionRequest(
    @field:NotNull(message = "The userId must not be blank or null.")
    @field:NotBlank(message = "The userId must not be blank or null.")
    @Schema(description = "The user ID whose conversion is to be fetched",
        example = "12345",
        required = true)
    val userId: String,

    @field:NotNull(message = "The source currency must not be null.")
    @field:NotBlank(message = "The source currency must not be blank.")
    @Schema(description = "Currency code to convert from (e.g., USD)", example = "USD", required = true)
    val fromCurrency: String,

    @field:NotNull(message = "The target currency must not be null.")
    @field:NotBlank(message = "The target currency must not be blank.")
    @Schema(description = "Currency code to convert to (e.g., BRL)", example = "BRL", required = true)
    val toCurrency: String,

    @field:NotNull(message = "The amount must not be null.")
    @field:Min(value = 0, message = "The amount must be greater than or equal to 0.")
    @Schema(description = "Amount of money to convert", example = "100.0", required = true)
    val amount: Double
)
