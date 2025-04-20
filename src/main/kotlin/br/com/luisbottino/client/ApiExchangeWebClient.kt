package br.com.luisbottino.client

import br.com.luisbottino.exception.CurrencyRateNotFoundException
import br.com.luisbottino.exception.ExternalServiceUnavailableException
import org.springframework.stereotype.Service

@Service
class ApiExchangeWebClient(
    private val config: ApiExchangeWebClientConfig,
    private val apiProperties: ApiProperties
) {

    fun getRate(from: String, to: String): Double {
        val response = config.apiExchangeWebClient()
            .get()
            .uri { uriBuilder ->
                val uri = uriBuilder.path(apiProperties.apiExchange.path)
                apiProperties.apiExchange.queryParams.forEach { (key, value) ->
                    uri.queryParam(key, value)
                }
                uri.build()
            }
            .retrieve()
            .bodyToMono(ApiExchangeRateResponse::class.java)
            .block() ?: throw ExternalServiceUnavailableException("Error fetching exchange rate")

        val rates = response.rates
        val conversionRate = rates[from]?.let { fromRate ->
            rates[to]?.let { toRate ->
                toRate / fromRate
            }
        }

        return conversionRate ?: throw CurrencyRateNotFoundException(from, to)
    }
}
