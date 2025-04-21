package br.com.luisbottino

import java.math.BigDecimal
import java.time.LocalDateTime

object TestData {

    const val PATH_API_V1_CONVERSIONS = "/api/v1/conversions"
    const val USER_ID_QUERY_PARAM = "userId"
    const val PAGE_QUERY_PARAM = "page"
    const val SIZE_QUERY_PARAM = "size"

    const val DEFAULT_TRANSACTION_ID = 1L
    const val DEFAULT_USER_ID = "user123"
    const val USD_CURRENCY = "USD"
    const val BRL_CURRENCY = "BRL"
    val DEFAULT_ORIGINAL_AMOUNT = BigDecimal("100.00")
    val DEFAULT_CONVERSION_RATE = BigDecimal("5.00")
    const val DEFAULT_PAGE_NUMBER = 0
    const val DEFAULT_PAGE_SIZE = 10
    val DEFAULT_TIMESTAMP: LocalDateTime = LocalDateTime.of(2025, 10, 25, 12, 0, 0)
    val TIMESTAMP_NOW: LocalDateTime = LocalDateTime.now()
}
