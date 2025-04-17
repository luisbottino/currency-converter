package br.com.luisbottino.controller.v1

import br.com.luisbottino.service.ConversionService
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/conversions")
@Validated
class CurrencyConversionControllerV1(
    private val conversionService: ConversionService
) : CurrencyConversionControllerV1Doc {

    private val logger: Logger = LoggerFactory.getLogger(CurrencyConversionControllerV1::class.java)

    @GetMapping
    override fun getConversionHistory(
        @Valid request: GetConversionHistoryRequest
    ): Slice<ConversionHistoryResponse> {
        logger.info("Received request to fetch conversion history for userId={}, page={}, size={}",
            request.userId, request.page, request.size)
        val pageable = PageRequest.of(request.page, request.size)
        val history = conversionService.getConversionHistory(request.userId, pageable)
        logger.info("Returning {} conversion(s) for userId={}", history.content.size, request.userId)
        return history
    }
}
