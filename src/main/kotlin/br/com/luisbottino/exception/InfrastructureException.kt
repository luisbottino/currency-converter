package br.com.luisbottino.exception

open class InfrastructureException(errorCode: ErrorCode, message : String) : ApiException(errorCode, message)
