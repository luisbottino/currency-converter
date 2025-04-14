package br.com.luisbottino.controller.v1

import br.com.luisbottino.service.ConversionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/conversions")
class CurrencyConversionControllerV1(
    private val conversionService: ConversionService
) {

    @GetMapping("/{userId}")
    fun getConversionHistory(@PathVariable userId: String): List<ConversionHistoryResponse> {
        return conversionService.getConversionHistory(userId)
    }
}