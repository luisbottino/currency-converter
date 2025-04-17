package br.com.luisbottino.mapper

import br.com.luisbottino.controller.v1.ConversionHistoryResponse
import br.com.luisbottino.model.Conversion
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger(Conversion::class.java)

fun Conversion.toConversionHistoryResponse(): ConversionHistoryResponse {
    logger.debug("Mapping Conversion(id={}) to ConversionHistoryResponse", this.id)
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
