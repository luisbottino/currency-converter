package br.com.luisbottino.controller.v1

import br.com.luisbottino.service.ConversionService
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/api/v1/conversions")
@Validated
class CurrencyConversionControllerV1(
    private val conversionService: ConversionService
) : CurrencyConversionControllerV1Doc {

    private val logger: Logger = LoggerFactory.getLogger(CurrencyConversionControllerV1::class.java)

    @GetMapping("/{userId}")
    override fun getConversionHistory(
        @PathVariable
        @NotBlank(message = "The userId must not be blank or null.")
        userId: String,
        @RequestParam(defaultValue = "0")
        @Min(value = 0, message = "The page number must not be negative.")
        page: Int,
        @RequestParam(defaultValue = "10")
        @Min(value = 1, message = "The page size must be at least 1.")
        size: Int,
    ): Slice<ConversionHistoryResponse> {
        logger.info("Received request to fetch conversion history for userId={}, page={}, size={}", userId, page, size)
        val pageable = PageRequest.of(page, size)
        val history = conversionService.getConversionHistory(userId, pageable)
        logger.info("Returning {} conversion(s) for userId={}", history.content.size, userId)
        return history
    }
}
