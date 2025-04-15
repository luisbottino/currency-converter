package br.com.luisbottino

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CurrencyConverterApp

fun main(args: Array<String>) {
    runApplication<CurrencyConverterApp>(*args)
}
