package br.com.luisbottino.service

import br.com.luisbottino.TestData
import br.com.luisbottino.client.ApiExchangeWebClient
import br.com.luisbottino.exception.CurrencyRateNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ExchangeRateServiceTest{
  private val exchangeWebClient = mockk<ApiExchangeWebClient>()
  private lateinit var service: ExchangeRateService

  @BeforeEach
  fun setUp() {
   service = ExchangeRateService(exchangeWebClient)
  }

  @Test
  fun givenValidRates_whenGetConversionRate_thenReturnCorrectValue() {
   val rates = mapOf(
    TestData.USD_CURRENCY to BigDecimal("1.00"),
    TestData.BRL_CURRENCY to TestData.DEFAULT_CONVERSION_RATE
   )
   every { exchangeWebClient.getRates(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) } returns rates

   val result = service.getConversionRate(TestData.USD_CURRENCY, TestData.BRL_CURRENCY)

   assertThat(result).isEqualByComparingTo(TestData.DEFAULT_CONVERSION_RATE.setScale(10))
  }

  @Test
  fun givenMissingFromRate_whenGetConversionRate_thenThrowCurrencyRateNotFoundException() {
   val rates = mapOf(TestData.BRL_CURRENCY to TestData.DEFAULT_CONVERSION_RATE)
   every { exchangeWebClient.getRates(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) } returns rates

   assertThatThrownBy { service.getConversionRate(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) }
    .isInstanceOf(CurrencyRateNotFoundException::class.java)
    .hasMessageContaining(TestData.USD_CURRENCY)
  }

  @Test
  fun givenMissingToRate_whenGetConversionRate_thenThrowCurrencyRateNotFoundException() {
   val rates = mapOf(TestData.USD_CURRENCY to BigDecimal("1.00"))
   every { exchangeWebClient.getRates(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) } returns rates

   assertThatThrownBy { service.getConversionRate(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) }
    .isInstanceOf(CurrencyRateNotFoundException::class.java)
    .hasMessageContaining(TestData.BRL_CURRENCY)
  }

  @Test
  fun givenEmptyRates_whenGetConversionRate_thenThrowCurrencyRateNotFoundException() {
   every { exchangeWebClient.getRates(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) } returns emptyMap()

   assertThatThrownBy { service.getConversionRate(TestData.USD_CURRENCY, TestData.BRL_CURRENCY) }
    .isInstanceOf(CurrencyRateNotFoundException::class.java)
    .hasMessageContaining(TestData.USD_CURRENCY)
    .hasMessageContaining(TestData.BRL_CURRENCY)
  }
 }
