package br.com.luisbottino.client

import br.com.luisbottino.TestData
import br.com.luisbottino.exception.ExternalServiceUnavailableException
import br.com.luisbottino.exception.InvalidApiKeyException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigDecimal


class ApiExchangeWebClientTest {

    private lateinit var server: MockWebServer
    private lateinit var client: ApiExchangeWebClient

    private val objectMapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()

        val mockBaseUrl = server.url("/").toString().removeSuffix("/")
        val properties = buildTestApiProperties(mockBaseUrl)

        val config = object : ApiExchangeWebClientConfig(properties) {
            override fun apiExchangeWebClient(): WebClient {
                return WebClient.builder()
                    .baseUrl(properties.apiExchange.baseUrl)
                    .build()
            }
        }

        client = ApiExchangeWebClient(config, properties)
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun givenValidApiResponse_whenGetRates_thenReturnRateMap() {
        val responseBody = objectMapper.writeValueAsString(
            ApiExchangeRateResponse(
                success = true,
                timestamp = 123456789,
                base = TestData.DEFAULT_BASE_CURRENCY,
                date = "2025-04-21",
                rates = mapOf(
                    TestData.USD_CURRENCY to BigDecimal("1.10"),
                    TestData.BRL_CURRENCY to BigDecimal("5.50")
                )
            )
        )

        server.enqueue(MockResponse().setResponseCode(200)
            .setBody(responseBody)
            .addHeader("Content-Type", "application/json"))

        val result = client.getRates(TestData.USD_CURRENCY, TestData.BRL_CURRENCY)

        assertThat(result).containsEntry(TestData.USD_CURRENCY, BigDecimal("1.10"))
        assertThat(result).containsEntry(TestData.BRL_CURRENCY, BigDecimal("5.50"))
    }

    @Test
    fun givenUnauthorizedResponse_whenGetRates_thenThrowInvalidApiKeyException() {
        server.enqueue(MockResponse().setResponseCode(401).setBody("Unauthorized"))

        assertThatThrownBy { client.getRates(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) }
            .isInstanceOf(InvalidApiKeyException::class.java)
    }

    @Test
    fun givenClientErrorResponse_whenGetRates_thenThrowExternalServiceUnavailableException() {
        server.enqueue(MockResponse().setResponseCode(400).setBody("Bad Request"))

        assertThatThrownBy { client.getRates(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) }
            .isInstanceOf(ExternalServiceUnavailableException::class.java)
            .hasMessageContaining("Client error from exchange API")
    }

    @Test
    fun givenServerErrorResponse_whenGetRates_thenThrowExternalServiceUnavailableException() {
        server.enqueue(MockResponse().setResponseCode(500).setBody("Internal Server Error"))

        assertThatThrownBy { client.getRates(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) }
            .isInstanceOf(ExternalServiceUnavailableException::class.java)
            .hasMessageContaining("Server error from exchange API")
    }

    @Test
    fun givenEmptyResponseBody_whenGetRates_thenThrowExternalServiceUnavailableException() {
        server.enqueue(MockResponse().setResponseCode(200).setBody(""))

        assertThatThrownBy { client.getRates(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) }
            .isInstanceOf(ExternalServiceUnavailableException::class.java)
            .hasMessageContaining("Empty response")
    }

    fun buildTestApiProperties(mockBaseUrl: String): ApiProperties {
        val apiProperties = ApiProperties()
        val apiDefinition = ApiDefinition().apply {
            baseUrl = mockBaseUrl
            path = "/v1/latest"
            queryParams = mapOf("access_key" to "test-key")
        }
        apiProperties.apiExchange = apiDefinition
        return apiProperties
    }
}
