package br.com.luisbottino

import br.com.luisbottino.model.Conversion
import java.time.LocalDateTime

data class ConversionTestData(
    val id: Long = TestData.DEFAULT_TRANSACTION_ID,
    val userId: String = TestData.DEFAULT_USER_ID,
    val fromCurrency: String = TestData.USD_CURRENCY,
    val toCurrency: String = TestData.BRL_CURRENCY,
    val originalAmount: Double = TestData.DEFAULT_ORIGINAL_AMOUNT,
    val conversionRate: Double = TestData.DEFAULT_CONVERSION_RATE,
    val timestamp: LocalDateTime = TestData.DEFAULT_TIMESTAMP
)

fun createConversion(data: ConversionTestData): Conversion {
    return Conversion(
        id = data.id,
        userId = data.userId,
        fromCurrency = data.fromCurrency,
        toCurrency = data.toCurrency,
        originalAmount = data.originalAmount,
        conversionRate = data.conversionRate,
        timestamp = data.timestamp
    )
}
