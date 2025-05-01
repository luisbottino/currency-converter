package br.com.luisbottino.service

import br.com.luisbottino.client.ApiExchangeWebClient
import br.com.luisbottino.exception.CurrencyRateNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class ExchangeRateService(
    private val exchangeWebClient: ApiExchangeWebClient
) {
    private val logger = LoggerFactory.getLogger(ExchangeRateService::class.java)

    companion object {
        private const val DIVISION_SCALE = 10
    }

    fun getConversionRate(from: String, to: String): BigDecimal {
        logger.debug("Calculating conversion rate from '{}' to '{}'", from, to)

        val rates = exchangeWebClient.getRates(from, to)

        val fromRate = rates[from]
        val toRate = rates[to]

        if (fromRate == null || toRate == null) {
            logger.warn(
                "Missing rate for conversion: fromRate={}, toRate={}, from='{}', to='{}'",
                fromRate, toRate, from, to
            )
            throw CurrencyRateNotFoundException(from, to)
        }

        val conversionRate = toRate.divide(fromRate, DIVISION_SCALE, RoundingMode.HALF_UP)
        logger.debug(
            "Conversion rate from '{}' to '{}' is {} ({} / {})",
            from, to, conversionRate, toRate, fromRate
        )

        return conversionRate
    }
}
