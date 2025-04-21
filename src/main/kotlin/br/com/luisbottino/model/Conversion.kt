package br.com.luisbottino.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
data class Conversion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: String = "",
    val fromCurrency: String = "",
    val originalAmount: BigDecimal = BigDecimal.ZERO,
    val toCurrency: String = "",
    val conversionRate: BigDecimal = BigDecimal.ZERO,
    val timestamp: java.time.LocalDateTime = java.time.LocalDateTime.now()
) {
    fun getConvertedAmount(): BigDecimal {
        return originalAmount.multiply(conversionRate)
    }
}
