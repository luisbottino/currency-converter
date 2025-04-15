package br.com.luisbottino.service


import br.com.luisbottino.controller.v1.ConversionHistoryResponse
import br.com.luisbottino.mapper.toConversionHistoryResponse
import br.com.luisbottino.model.Conversion
import br.com.luisbottino.repository.ConversionRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service

@Service
class ConversionService(private val repository: ConversionRepository) {

    fun getConversionHistory(userId: String, pageable: Pageable): Slice<ConversionHistoryResponse> {
        val conversions: Slice<Conversion> = repository.findAllByUserId(userId, pageable)
        return conversions.map { it.toConversionHistoryResponse() }
    }
}
