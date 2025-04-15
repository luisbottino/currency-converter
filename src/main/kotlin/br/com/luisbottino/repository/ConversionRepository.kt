package br.com.luisbottino.repository

import br.com.luisbottino.model.Conversion
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface ConversionRepository : JpaRepository<Conversion, Long> {
    fun findAllByUserId(userId: String, pageable: Pageable): Slice<Conversion>
}
