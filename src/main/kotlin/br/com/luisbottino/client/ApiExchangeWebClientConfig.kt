package br.com.luisbottino.client

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ApiExchangeWebClientConfig(
    private val apiProperties: ApiProperties
) {

    fun apiExchangeWebClient(): WebClient = WebClient.builder()
        .baseUrl(apiProperties.apiExchange.baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()
}
