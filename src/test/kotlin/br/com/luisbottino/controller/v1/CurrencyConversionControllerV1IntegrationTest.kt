package br.com.luisbottino.controller.v1

import br.com.luisbottino.ConversionTestData
import br.com.luisbottino.PropertyOverrideContextInitializer
import br.com.luisbottino.TestData
import br.com.luisbottino.createConversion
import br.com.luisbottino.repository.ConversionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.hasItem
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ContextConfiguration(initializers = [PropertyOverrideContextInitializer::class])
class CurrencyConversionControllerV1IntegrationTest
@Autowired constructor(
    private val mockMvc: MockMvc,
    private val repository: ConversionRepository,
    private val objectMapper: ObjectMapper
) {

    companion object {
        private lateinit var server: MockWebServer

        @JvmStatic
        @BeforeAll
        fun startServer() {
            server = MockWebServer()
            server.start()
        }

        @JvmStatic
        @AfterAll
        fun stopServer() {
            server.shutdown()
        }

        fun getBaseUrl(): String = server.url("/").toString().removeSuffix("/")
    }

    @BeforeEach
    fun setup() {
        repository.deleteAll()
        val conversion = createConversion(ConversionTestData())
        repository.save(conversion)
    }

    @Nested
    inner class GetConversionHistory {
        @Test
        fun givenExistingUserId_whenGetConversionHistory_thenReturnPagedConversions() {
            mockMvc.perform(
                get(TestData.PATH_API_V1_CONVERSIONS)
                    .queryParam(TestData.USER_ID_QUERY_PARAM, TestData.DEFAULT_USER_ID)
                    .queryParam(TestData.PAGE_QUERY_PARAM, TestData.DEFAULT_PAGE_NUMBER.toString())
                    .queryParam(TestData.SIZE_QUERY_PARAM, TestData.DEFAULT_PAGE_SIZE.toString())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray)
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].userId").value(TestData.DEFAULT_USER_ID))
                .andExpect(jsonPath("$.content[0].fromCurrency").value(TestData.USD_CURRENCY))
                .andExpect(jsonPath("$.content[0].toCurrency").value(TestData.BRL_CURRENCY))
                .andExpect(jsonPath("$.content[0].originalAmount")
                    .value(TestData.DEFAULT_ORIGINAL_AMOUNT.toDouble()))
                .andExpect(jsonPath("$.content[0].conversionRate")
                    .value(TestData.DEFAULT_CONVERSION_COTATION.toDouble()))
        }

        @Test
        fun givenNonExistentUserId_whenGetConversionHistory_thenReturnEmptyContent() {
            mockMvc.perform(
                get(TestData.PATH_API_V1_CONVERSIONS)
                    .queryParam(TestData.USER_ID_QUERY_PARAM, "nonexistent-user")
                    .queryParam(TestData.PAGE_QUERY_PARAM, TestData.DEFAULT_PAGE_NUMBER.toString())
                    .queryParam(TestData.SIZE_QUERY_PARAM, TestData.DEFAULT_PAGE_SIZE.toString())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray)
                .andExpect(jsonPath("$.content.length()").value(0))
        }

        @Test
        fun givenUserIdNotInformed_whenGetConversionHistory_thenReturnNotFound() {
            mockMvc.perform(
                get(TestData.PATH_API_V1_CONVERSIONS)
                    .queryParam(TestData.USER_ID_QUERY_PARAM, "")
                    .queryParam(TestData.PAGE_QUERY_PARAM, TestData.DEFAULT_PAGE_NUMBER.toString())
                    .queryParam(TestData.SIZE_QUERY_PARAM, TestData.DEFAULT_PAGE_SIZE.toString())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value("userId: The userId must not be blank or null."))
        }

        @Test
        fun givenInvalidPage_whenGetConversionHistory_thenReturnBadRequest() {
            mockMvc.perform(
                get(TestData.PATH_API_V1_CONVERSIONS)
                    .queryParam(TestData.USER_ID_QUERY_PARAM, TestData.DEFAULT_USER_ID)
                    .queryParam(TestData.PAGE_QUERY_PARAM, "-1")
                    .queryParam(TestData.SIZE_QUERY_PARAM, TestData.DEFAULT_PAGE_SIZE.toString())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value("page: The page number must not be negative."))
        }

        @Test
        fun givenInvalidSize_whenGetConversionHistory_thenReturnBadRequest() {
            mockMvc.perform(
                get(TestData.PATH_API_V1_CONVERSIONS)
                    .queryParam(TestData.USER_ID_QUERY_PARAM, TestData.DEFAULT_USER_ID)
                    .queryParam(TestData.PAGE_QUERY_PARAM, TestData.DEFAULT_PAGE_NUMBER.toString())
                    .queryParam(TestData.SIZE_QUERY_PARAM, "0")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value("size: The page size must be at least 1."))
        }
    }

    @Nested
    inner class PostConversion {

        @Test
        fun givenValidConversionRequest_whenPostConversion_thenReturnSuccess() {
            val mockApiResponse = mapOf(
                "success" to true,
                "timestamp" to 123456789,
                "base" to "USD",
                "date" to "2025-04-21",
                "rates" to mapOf(
                    "USD" to BigDecimal("1.0"),
                    "BRL" to BigDecimal("5.0")
                )
            )
            server.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(objectMapper.writeValueAsString(mockApiResponse)))

            val request = mapOf(
                "userId" to TestData.DEFAULT_USER_ID,
                "fromCurrency" to TestData.BRL_CURRENCY,
                "toCurrency" to TestData.USD_CURRENCY,
                "amount" to TestData.DEFAULT_ORIGINAL_AMOUNT
            )

            mockMvc.perform(
                post(TestData.PATH_API_V1_CONVERSIONS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(TestData.DEFAULT_USER_ID))
                .andExpect(jsonPath("$.fromCurrency").value(TestData.BRL_CURRENCY))
                .andExpect(jsonPath("$.toCurrency").value(TestData.USD_CURRENCY))
                .andExpect(jsonPath("$.originalAmount").value(TestData.DEFAULT_ORIGINAL_AMOUNT.toDouble()))
                .andExpect(jsonPath("$.conversionRate").value(0.2))
                .andExpect(jsonPath("$.convertedAmount").value(20.0))
        }

        @Test
        fun givenMissingAmount_whenPostConversion_thenReturnBadRequest() {
            val request = mapOf(
                "userId" to TestData.DEFAULT_USER_ID,
                "fromCurrency" to TestData.USD_CURRENCY,
                "toCurrency" to TestData.BRL_CURRENCY
            )

            mockMvc.perform(
                post(TestData.PATH_API_V1_CONVERSIONS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value("amount: The amount must not be null."))
        }

        @Test
        fun givenMissingUserId_whenPostConversion_thenReturnBadRequest() {
            val request = mapOf(
                "fromCurrency" to TestData.USD_CURRENCY,
                "toCurrency" to TestData.BRL_CURRENCY,
                "amount" to TestData.DEFAULT_ORIGINAL_AMOUNT
            )

            mockMvc.perform(
                post(TestData.PATH_API_V1_CONVERSIONS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message", hasItem("userId: The userId must not be blank or null.")))
        }

        @Test
        fun givenFromCurrency_whenPostConversion_thenReturnBadRequest() {
            val request = mapOf(
                "userId" to TestData.DEFAULT_USER_ID,
                "toCurrency" to TestData.BRL_CURRENCY,
                "amount" to TestData.DEFAULT_ORIGINAL_AMOUNT
            )

            mockMvc.perform(
                post(TestData.PATH_API_V1_CONVERSIONS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message", hasItem("fromCurrency: The source currency must not be blank.")))
        }

        @Test
        fun givenToCurrency_whenPostConversion_thenReturnBadRequest() {
            val request = mapOf(
                "userId" to TestData.DEFAULT_USER_ID,
                "fromCurrency" to TestData.USD_CURRENCY,
                "amount" to TestData.DEFAULT_ORIGINAL_AMOUNT
            )

            mockMvc.perform(
                post(TestData.PATH_API_V1_CONVERSIONS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message", hasItem("toCurrency: The target currency must not be blank.")))
        }

        @Test
        fun givenNegativeAmount_whenPostConversion_thenReturnBadRequest() {
            val request = mapOf(
                "userId" to TestData.DEFAULT_USER_ID,
                "fromCurrency" to TestData.USD_CURRENCY,
                "toCurrency" to TestData.BRL_CURRENCY,
                "amount" to BigDecimal("-1.00")
            )

            mockMvc.perform(
                post(TestData.PATH_API_V1_CONVERSIONS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").value("amount: The amount must be greater than or equal to 0."))
        }
    }
}
