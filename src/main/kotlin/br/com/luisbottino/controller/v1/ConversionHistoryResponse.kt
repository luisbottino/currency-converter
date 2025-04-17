package br.com.luisbottino.controller.v1

import java.time.LocalDateTime

data class ConversionHistoryResponse(
    val transactionId: String,
    val userId: String,
    val fromCurrency: String,
    val originalAmount: Double,
    val toCurrency: String,
    val convertedAmount: Double,
    val conversionRate: Double,
    val timestamp: LocalDateTime
)
