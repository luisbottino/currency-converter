package br.com.luisbottino.service


import br.com.luisbottino.controller.v1.ConversionHistoryResponse
import br.com.luisbottino.mapper.toConversionHistoryResponse
import br.com.luisbottino.model.Conversion
import br.com.luisbottino.repository.ConversionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service

@Service
class ConversionService(private val repository: ConversionRepository) {

    private val logger: Logger = LoggerFactory.getLogger(ConversionService::class.java)

    fun getConversionHistory(userId: String, pageable: Pageable): Slice<ConversionHistoryResponse> {
        logger.debug("Fetching conversion history for userId={} with pageable={}", userId, pageable)
        val conversions: Slice<Conversion> = repository.findAllByUserId(userId, pageable)
        logger.info("Fetched {} conversion(s) for userId={}", conversions.content.size, userId)
        return conversions.map { it.toConversionHistoryResponse() }
    }
}
