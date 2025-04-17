package br.com.luisbottino.controller.v1

import br.com.luisbottino.ConversionTestData
import br.com.luisbottino.TestData
import br.com.luisbottino.createConversion
import br.com.luisbottino.repository.ConversionRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CurrencyConversionControllerV1IntegrationTest
@Autowired constructor(
    private val mockMvc: MockMvc,
    private val repository: ConversionRepository
) {

    @BeforeEach
    fun setup() {
        repository.deleteAll()
        val conversion = createConversion(ConversionTestData())
        repository.save(conversion)
    }

    @Test
    fun givenExistingUserId_whenGetConversionHistory_thenReturnPagedConversions() {
        mockMvc.perform(
            get(TestData.PATH_API_V1_CONVERSIONS.plus(TestData.DEFAULT_USER_ID))
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
            .andExpect(jsonPath("$.content[0].originalAmount").value(TestData.DEFAULT_ORIGINAL_AMOUNT))
            .andExpect(jsonPath("$.content[0].conversionRate").value(TestData.DEFAULT_CONVERSION_RATE))
    }

    @Test
    fun givenNonExistentUserId_whenGetConversionHistory_thenReturnEmptyContent() {
        mockMvc.perform(
            get(TestData.PATH_API_V1_CONVERSIONS + "nonexistent-user")
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
                .queryParam(TestData.PAGE_QUERY_PARAM, TestData.DEFAULT_PAGE_NUMBER.toString())
                .queryParam(TestData.SIZE_QUERY_PARAM, TestData.DEFAULT_PAGE_SIZE.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun givenInvalidPage_whenGetConversionHistory_thenReturnBadRequest() {
        mockMvc.perform(
            get(TestData.PATH_API_V1_CONVERSIONS.plus(TestData.DEFAULT_USER_ID))
                .queryParam(TestData.PAGE_QUERY_PARAM, "-1")
                .queryParam(TestData.SIZE_QUERY_PARAM, TestData.DEFAULT_PAGE_SIZE.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("The page number must not be negative."))
    }

    @Test
    fun givenInvalidSize_whenGetConversionHistory_thenReturnBadRequest() {
        mockMvc.perform(
            get(TestData.PATH_API_V1_CONVERSIONS.plus(TestData.DEFAULT_USER_ID))
                .queryParam(TestData.PAGE_QUERY_PARAM, TestData.DEFAULT_PAGE_NUMBER.toString())
                .queryParam(TestData.SIZE_QUERY_PARAM, "0")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("The page size must be at least 1."))
    }
}
