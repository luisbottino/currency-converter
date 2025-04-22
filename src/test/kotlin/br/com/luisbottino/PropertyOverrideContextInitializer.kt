package br.com.luisbottino

import br.com.luisbottino.controller.v1.CurrencyConversionControllerV1IntegrationTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class PropertyOverrideContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(context: ConfigurableApplicationContext) {
        val baseUrl = CurrencyConversionControllerV1IntegrationTest.getBaseUrl()
        TestPropertyValues.of(
            "apis.api-exchange.base-url=$baseUrl",
            "apis.api-exchange.path=/v1/latest",
            "apis.api-exchange.query-params.access_key=test-key"
        ).applyTo(context.environment)
    }
}
