package br.com.luisbottino.repository

import br.com.luisbottino.model.Conversion
import org.springframework.data.jpa.repository.JpaRepository

interface ConversionRepository : JpaRepository<Conversion, Long> {
    fun findAllByUserId(userId: String): List<Conversion>
}