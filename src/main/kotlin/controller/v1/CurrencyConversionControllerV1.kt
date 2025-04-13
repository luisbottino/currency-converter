package br.com.luisbottino.controller.v1

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/conversions")
class CurrencyConversionControllerV1 {

    @GetMapping("/{userId}")
    fun getConversionHistory(@PathVariable userId: String): List<ConversionHistoryResponse> {
        return listOf(
            ConversionHistoryResponse(
            transactionId = UUID.randomUUID().toString(),
            userId = userId,
            fromCurrency = "USD",
            originalAmount = 100.0,
            toCurrency = "BRL",
            convertedAmount = 1800.0,
            conversionRate = 1.8,
            timestamp = java.time.LocalDateTime.now()
        )
        )
    }
}