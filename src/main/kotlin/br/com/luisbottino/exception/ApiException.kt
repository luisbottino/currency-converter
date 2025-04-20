package br.com.luisbottino.exception

open class ApiException(
    val errorCode: ErrorCode,
    override val message: String
): RuntimeException(message)
