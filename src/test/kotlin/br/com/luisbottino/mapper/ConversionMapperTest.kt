package br.com.luisbottino.mapper

import br.com.luisbottino.ConversionTestData
import br.com.luisbottino.TestData
import br.com.luisbottino.createConversion
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

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
        assertThat(result.conversionRate).isEqualTo(TestData.DEFAULT_CONVERSION_COTATION)
        assertThat(result.convertedAmount).usingComparator(BigDecimal::compareTo).isEqualTo(BigDecimal("500"))
        assertThat(result.timestamp).isEqualTo(TestData.DEFAULT_TIMESTAMP)
    }

    @Test
    fun givenConversionWithExtremeValues_whenToConversionHistoryResponse_thenMapValuesCorrectly() {
        val conversion = createConversion(
            ConversionTestData(
                originalAmount = BigDecimal.valueOf(Double.MAX_VALUE),
                conversionRate = BigDecimal.TWO
            )
        )

        val result = conversion.toConversionHistoryResponse()

        assertThat(result.originalAmount).usingComparator(BigDecimal::compareTo)
            .isEqualTo(BigDecimal.valueOf(Double.MAX_VALUE))
        assertThat(result.conversionRate).usingComparator(BigDecimal::compareTo)
            .isEqualTo(BigDecimal.TWO)
        assertThat(result.convertedAmount).usingComparator(BigDecimal::compareTo)
            .isEqualTo(BigDecimal.valueOf(Double.MAX_VALUE).multiply(BigDecimal.TWO))
    }
}
