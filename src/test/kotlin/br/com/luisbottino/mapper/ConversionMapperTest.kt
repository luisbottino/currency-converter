package br.com.luisbottino.mapper

import br.com.luisbottino.ConversionTestData
import br.com.luisbottino.TestData
import br.com.luisbottino.createConversion
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConversionMapperTest {

    @Test
    fun givenConversion_whenToConversionHistoryResponse_thenReturnConversionHistoryResponse() {
        val conversion = createConversion(ConversionTestData())

        val result = conversion.toConversionHistoryResponse()

        assertThat(result.transactionId).isEqualTo("1")
        assertThat(result.userId).isEqualTo(TestData.DEFAULT_USER_ID)
        assertThat(result.fromCurrency).isEqualTo(TestData.USD_CURRENCY)
        assertThat(result.toCurrency).isEqualTo(TestData.BRL_CURRENCY)
        assertThat(result.originalAmount).isEqualTo(TestData.DEFAULT_ORIGINAL_AMOUNT)
        assertThat(result.conversionRate).isEqualTo(TestData.DEFAULT_CONVERSION_RATE)
        assertThat(result.convertedAmount).isEqualTo(500.0)
        assertThat(result.timestamp).isEqualTo(TestData.DEFAULT_TIMESTAMP)
    }

    @Test
    fun givenConversionWithExtremeValues_whenToConversionHistoryResponse_thenMapValuesCorrectly() {
        val conversion = createConversion(
            ConversionTestData(
                originalAmount = Double.MAX_VALUE,
                conversionRate = 2.0
            )
        )

        val result = conversion.toConversionHistoryResponse()

        assertThat(result.originalAmount).isEqualTo(Double.MAX_VALUE)
        assertThat(result.conversionRate).isEqualTo(2.0)
        assertThat(result.convertedAmount).isEqualTo(Double.MAX_VALUE * 2.0)
    }
}
