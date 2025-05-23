package br.com.luisbottino.service


import br.com.luisbottino.controller.v1.ConversionHistoryResponse
import br.com.luisbottino.controller.v1.PostConversionRequest
import br.com.luisbottino.mapper.toConversionHistoryResponse
import br.com.luisbottino.model.Conversion
import br.com.luisbottino.repository.ConversionRepository
import br.com.luisbottino.util.requireField
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ConversionService(
    private val repository: ConversionRepository,
    private val exchangeRateService: ExchangeRateService
) {

    private val logger: Logger = LoggerFactory.getLogger(ConversionService::class.java)

    fun getConversionHistory(userId: String, pageable: Pageable): Slice<ConversionHistoryResponse> {
        logger.debug("Fetching conversion history for userId={} with pageable={}", userId, pageable)
        val conversions: Slice<Conversion> = repository.findAllByUserId(userId, pageable)
        logger.info("Fetched {} conversion(s) for userId={}", conversions.content.size, userId)
        return conversions.map { it.toConversionHistoryResponse() }
    }

    fun convertCurrency(conversionRequest: PostConversionRequest): ConversionHistoryResponse {
        val userId = requireField(conversionRequest.userId, "UserId")
        val fromCurrency = requireField(conversionRequest.fromCurrency, "FromCurrency")
        val toCurrency = requireField(conversionRequest.toCurrency, "ToCurrency")
        val amount = requireField(conversionRequest.amount, "Amount")

        val conversionRate = exchangeRateService.getConversionRate(fromCurrency, toCurrency)

        val conversion = Conversion(
            userId = userId,
            fromCurrency = fromCurrency,
            toCurrency = toCurrency,
            originalAmount = amount,
            conversionRate = conversionRate,
            timestamp = LocalDateTime.now()
        )

        logger.debug("Conversion calculated: from={} to={}, rate={}, amount={}, result={}",
            conversion.fromCurrency,
            conversion.toCurrency,
            conversion.conversionRate,
            conversion.originalAmount,
            conversion.getConvertedAmount()
        )

        val conversionSaved = repository.save(conversion)

        logger.debug("Conversion saved: id={}, userId={}, timestamp={}",
            conversionSaved.id, conversionSaved.userId, conversionSaved.timestamp
        )
        return conversionSaved.toConversionHistoryResponse()
    }
}
