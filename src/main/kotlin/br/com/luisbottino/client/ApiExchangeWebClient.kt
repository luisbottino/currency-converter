package br.com.luisbottino.client

import br.com.luisbottino.exception.ExternalServiceUnavailableException
import br.com.luisbottino.exception.InvalidApiKeyException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ApiExchangeWebClient(
    private val config: ApiExchangeWebClientConfig,
    private val apiProperties: ApiProperties
) {

    private val logger = LoggerFactory.getLogger(ApiExchangeWebClient::class.java)

    fun getRates(from: String, to: String): Map<String, BigDecimal> {
        logger.debug("Requesting exchange rates from '{}' to '{}'", from, to)
        val response = config.apiExchangeWebClient()
            .get()
            .uri { uriBuilder ->
                val uriBuilderWithParams = uriBuilder.path(apiProperties.apiExchange.path)
                apiProperties.apiExchange.queryParams.forEach { (key, value) ->
                    uriBuilderWithParams.queryParam(key, value)
                }
                val uri = uriBuilderWithParams.build()
                logger.debug("Final URI for exchange request: {}", uri)

                uri
            }
            .retrieve()
            .onStatus({ it == HttpStatus.UNAUTHORIZED }) {
                logger.warn("Received 401 Unauthorized from exchange API - likely invalid API key")
                it.bodyToMono(String::class.java).map {
                    InvalidApiKeyException()
                }
            }
            .onStatus({ it.is4xxClientError }) {
                it.bodyToMono(String::class.java).map { body ->
                    logger.error("Client error from exchange API (4xx): {}", body)
                    ExternalServiceUnavailableException("Client error from exchange API: $body")
                }
            }
            .onStatus({ it.is5xxServerError }) {
                it.bodyToMono(String::class.java).map { body ->
                    logger.error("Server error from exchange API (5xx): {}", body)
                    ExternalServiceUnavailableException("Server error from exchange API: $body")
                }
            }
            .bodyToMono(ApiExchangeRateResponse::class.java)
            .block()
            ?: run {
                logger.error("Empty or null response from exchange API")
                throw ExternalServiceUnavailableException("Empty response from exchange rate API")
            }

        logger.debug("Successfully retrieved rates for '{} -> {}': {}", from, to, response.rates)
        return response.rates
    }
}
