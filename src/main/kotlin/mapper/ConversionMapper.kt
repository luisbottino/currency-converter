package br.com.luisbottino.mapper

import br.com.luisbottino.controller.v1.ConversionHistoryResponse
import br.com.luisbottino.model.Conversion

fun Conversion.toConversionHistoryResponse(): ConversionHistoryResponse {
    return ConversionHistoryResponse(
        transactionId = id.toString(),
        userId = userId,
        fromCurrency = fromCurrency,
        originalAmount = originalAmount,
        toCurrency = toCurrency,
        convertedAmount = getConvertedAmount(),
        conversionRate = conversionRate,
        timestamp = timestamp
    )
}
