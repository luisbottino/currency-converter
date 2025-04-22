package br.com.luisbottino.model

import br.com.luisbottino.ConversionTestData
import br.com.luisbottino.createConversion
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ConversionTest {

    @Test
    fun givenPositiveOriginalAmountAndConversionRate_whenGetConvertedAmount_thenReturnCorrectValue() {
        val conversion = createConversion(ConversionTestData())
        val result = conversion.getConvertedAmount()

        assertThat(result).usingComparator(BigDecimal::compareTo).isEqualTo(BigDecimal("500"))
    }

    @Test
    fun givenOriginalAmountZero_whenGetConvertedAmount_thenReturnZero() {
        val conversion = createConversion(ConversionTestData(originalAmount = BigDecimal.ZERO))
        val result = conversion.getConvertedAmount()

        assertThat(result).usingComparator(BigDecimal::compareTo).isEqualTo(BigDecimal.ZERO)
    }
    @Test
    fun givenConversionRateZero_whenGetConvertedAmount_thenReturnZero() {
        val conversion = createConversion(ConversionTestData(conversionRate = BigDecimal.ZERO))
        val result = conversion.getConvertedAmount()

        assertThat(result).usingComparator(BigDecimal::compareTo).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun givenExtremeValues_whenGetConvertedAmount_thenHandleCorrectly() {
        val conversion = createConversion(
            ConversionTestData(
                originalAmount = BigDecimal.valueOf(Double.MAX_VALUE), conversionRate = BigDecimal.ONE
            )
        )
        val result = conversion.getConvertedAmount()

        assertThat(result).isEqualTo(BigDecimal.valueOf(Double.MAX_VALUE))
    }
}
