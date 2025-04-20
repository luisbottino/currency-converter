package br.com.luisbottino.exception

class ExternalServiceUnavailableException(service: String) : InfrastructureException(
    errorCode = ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
    message = "Failed to retrieve exchange rate from $service"
)
