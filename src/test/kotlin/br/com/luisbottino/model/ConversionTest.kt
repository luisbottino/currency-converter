package br.com.luisbottino.model

import br.com.luisbottino.ConversionTestData
import br.com.luisbottino.createConversion
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConversionTest {

    @Test
    fun givenPositiveOriginalAmountAndConversionRate_whenGetConvertedAmount_thenReturnCorrectValue() {
        val conversion = createConversion(ConversionTestData())
        val result = conversion.getConvertedAmount()

        assertThat(result).isEqualTo(500.0)
    }

    @Test
    fun givenOriginalAmountZero_whenGetConvertedAmount_thenReturnZero() {
        val conversion = createConversion(ConversionTestData(originalAmount = 0.0))
        val result = conversion.getConvertedAmount()

        assertThat(result).isEqualTo(0.0) // 0.0 * 5.0 = 0.0
    }
    @Test
    fun givenConversionRateZero_whenGetConvertedAmount_thenReturnZero() {
        val conversion = createConversion(ConversionTestData(conversionRate = 0.0))
        val result = conversion.getConvertedAmount()

        assertThat(result).isEqualTo(0.0) // 100.0 * 0.0 = 0.0
    }

    @Test
    fun givenExtremeValues_whenGetConvertedAmount_thenHandleCorrectly() {
        val conversion = createConversion(ConversionTestData(originalAmount = Double.MAX_VALUE, conversionRate = 1.0))
        val result = conversion.getConvertedAmount()

        assertThat(result).isEqualTo(Double.MAX_VALUE)
    }
}
