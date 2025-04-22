package br.com.luisbottino.client

import java.math.BigDecimal

data class ApiExchangeRateResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, BigDecimal>
)
