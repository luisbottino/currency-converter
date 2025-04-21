package br.com.luisbottino.service

import br.com.luisbottino.ConversionTestData
import br.com.luisbottino.TestData
import br.com.luisbottino.controller.v1.ConversionHistoryResponse
import br.com.luisbottino.createConversion
import br.com.luisbottino.model.Conversion
import br.com.luisbottino.repository.ConversionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.SliceImpl

class ConversionServiceTest {

    private val repository: ConversionRepository = mockk()
    private val exchangeRateService: ExchangeRateService = mockk()
    private lateinit var service: ConversionService

    private val pageable = PageRequest.of(TestData.DEFAULT_PAGE_NUMBER, TestData.DEFAULT_PAGE_SIZE)


    @BeforeEach
    fun setUp() {
        service = ConversionService(repository, exchangeRateService)
    }

    @Test
    fun givenUserWithConversions_whenGetConversionHistory_thenReturnsConversions() {
        val conversion = createConversion(ConversionTestData(timestamp = TestData.TIMESTAMP_NOW))
        val expectedResponse = createExpectedConversionHistoryResponse(conversion)
        val conversions = SliceImpl(listOf(conversion), pageable, false)

        every { repository.findAllByUserId(TestData.DEFAULT_USER_ID, pageable) } returns conversions

        val result = service.getConversionHistory(TestData.DEFAULT_USER_ID, pageable)

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0]).isEqualTo(expectedResponse)

        verify(exactly = 1) { repository.findAllByUserId(TestData.DEFAULT_USER_ID, pageable) }
    }

    @Test
    fun givenUserWithoutConversions_whenGetConversionHistory_thenReturnsEmptyList() {
        every {
            repository.findAllByUserId(TestData.DEFAULT_USER_ID, pageable)
        } returns SliceImpl(emptyList(), pageable, false)

        val result = service.getConversionHistory(TestData.DEFAULT_USER_ID, pageable)

        assertThat(result.content).isEmpty()
        verify(exactly = 1) { repository.findAllByUserId(TestData.DEFAULT_USER_ID, pageable) }
    }

    @Test
    fun givenDatabaseError_whenGetConversionHistory_thenThrowsException() {
        every {
            repository.findAllByUserId(TestData.DEFAULT_USER_ID, pageable)
        } throws RuntimeException("Database error")

        assertThatThrownBy { service.getConversionHistory(TestData.DEFAULT_USER_ID, pageable) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Database error")
    }

    private fun createExpectedConversionHistoryResponse(conversion: Conversion): ConversionHistoryResponse {
        return ConversionHistoryResponse(
            transactionId = conversion.id.toString(),
            userId = conversion.userId,
            fromCurrency = conversion.fromCurrency,
            originalAmount = conversion.originalAmount,
            toCurrency = conversion.toCurrency,
            convertedAmount = conversion.getConvertedAmount(),
            conversionRate = conversion.conversionRate,
            timestamp = conversion.timestamp
        )
    }

}
