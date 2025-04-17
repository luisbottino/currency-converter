package br.com.luisbottino.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Conversion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: String = "",
    val fromCurrency: String = "",
    val originalAmount: Double = 0.0,
    val toCurrency: String = "",
    val conversionRate: Double = 0.0,
    val timestamp: java.time.LocalDateTime = java.time.LocalDateTime.now()
) {
    fun getConvertedAmount(): Double {
        return originalAmount * conversionRate
    }
}
