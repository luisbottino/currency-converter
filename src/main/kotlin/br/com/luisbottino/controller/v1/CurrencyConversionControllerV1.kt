package br.com.luisbottino.controller.v1

import br.com.luisbottino.service.ConversionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/api/v1/conversions")
class CurrencyConversionControllerV1(
    private val conversionService: ConversionService
) {

    private val logger: Logger = LoggerFactory.getLogger(CurrencyConversionControllerV1::class.java)

    @GetMapping("/{userId}")
    fun getConversionHistory(
        @PathVariable userId: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): Slice<ConversionHistoryResponse> {
        logger.info("Received request to fetch conversion history for userId={}, page={}, size={}", userId, page, size)
        val pageable = PageRequest.of(page, size)
        val history = conversionService.getConversionHistory(userId, pageable)
        logger.info("Returning {} conversion(s) for userId={}", history.content.size, userId)
        return history
    }
}
