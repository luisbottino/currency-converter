package br.com.luisbottino.controller.v1

import br.com.luisbottino.config.BigDecimalTwoDigitsSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.math.BigDecimal
import java.time.LocalDateTime

data class ConversionHistoryResponse(
    val transactionId: String,
    val userId: String,
    val fromCurrency: String,
    @JsonSerialize(using = BigDecimalTwoDigitsSerializer::class)
    val originalAmount: BigDecimal,
    val toCurrency: String,
    @JsonSerialize(using = BigDecimalTwoDigitsSerializer::class)
    val convertedAmount: BigDecimal,
    @JsonSerialize(using = BigDecimalTwoDigitsSerializer::class)
    val conversionRate: BigDecimal,
    val timestamp: LocalDateTime
)
