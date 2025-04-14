package br.com.luisbottino.service

import br.com.luisbottino.controller.v1.ConversionHistoryResponse
import br.com.luisbottino.mapper.toConversionHistoryResponse
import br.com.luisbottino.model.Conversion
import br.com.luisbottino.repository.ConversionRepository
import org.springframework.stereotype.Service

@Service
class ConversionService(private val repository: ConversionRepository) {

    fun getConversionHistory(userId: String): List<ConversionHistoryResponse> {
        val conversions: List<Conversion> = repository.findAllByUserId(userId)
        return conversions.map { it.toConversionHistoryResponse() }
    }
}