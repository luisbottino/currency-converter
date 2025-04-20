package br.com.luisbottino.exception

open class BusinessException(errorCode: ErrorCode, message: String) : ApiException(errorCode, message)
