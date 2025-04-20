package br.com.luisbottino.controller.v1

import java.time.LocalDateTime

data class ApiErrorResponse(
    val timestamp: String = LocalDateTime.now().toString(),
    val status: Int,
    val error: String,
    val code: String,
    val message: Any,
    val path: String
)
