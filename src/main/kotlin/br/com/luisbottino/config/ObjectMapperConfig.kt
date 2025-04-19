package br.com.luisbottino.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean

@Bean
fun objectMapper(): ObjectMapper = jacksonObjectMapper().registerKotlinModule()
