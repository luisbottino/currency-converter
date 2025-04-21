package br.com.luisbottino.controller.v1

import br.com.luisbottino.config.BigDecimalTwoDigitsSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "Represents a historical record of a currency conversion performed by a user.")
data class ConversionHistoryResponse(

    @Schema(
        description = "Unique identifier for the conversion transaction.",
        example = "85"
    )
    val transactionId: String,

    @Schema(
        description = "The user identifier who performed the conversion.",
        example = "luis_bottino"
    )
    val userId: String,

    @Schema(
        description = "Currency code from which the amount was converted.",
        example = "USD"
    )
    val fromCurrency: String,

    @JsonSerialize(using = BigDecimalTwoDigitsSerializer::class)
    @Schema(
        description = "Original amount provided for conversion.",
        example = "70.00"
    )
    val originalAmount: BigDecimal,

    @Schema(
        description = "Currency code to which the amount was converted.",
        example = "BRL"
    )
    val toCurrency: String,

    @JsonSerialize(using = BigDecimalTwoDigitsSerializer::class)
    @Schema(
        description = "Amount after conversion, calculated based on the exchange rate.",
        example = "406.57"
    )
    val convertedAmount: BigDecimal,

    @JsonSerialize(using = BigDecimalTwoDigitsSerializer::class)
    @Schema(
        description = "Exchange rate used to perform the conversion.",
        example = "5.81"
    )
    val conversionRate: BigDecimal,

    @Schema(
        description = "Timestamp of when the conversion was performed.",
        example = "2025-04-21T13:32:33.519289"
    )
    val timestamp: LocalDateTime
)
