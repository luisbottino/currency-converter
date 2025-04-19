package br.com.luisbottino.client

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
            .block() ?: throw RuntimeException("Error fetching exchange rate")

        val rates = response.rates
        val fromRate = rates[from]
            ?: throw IllegalArgumentException("Missing rate for $from")
        val toRate = rates[to]
            ?: throw IllegalArgumentException("Missing rate for $to")

        return toRate / fromRate
    }
}
