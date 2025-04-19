package br.com.luisbottino.client

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "apis")
class ApiProperties {
    lateinit var apiExchange: ApiDefinition
}

class ApiDefinition {
    lateinit var baseUrl: String
    lateinit var path: String
    var queryParams: Map<String, String> = emptyMap()
}
