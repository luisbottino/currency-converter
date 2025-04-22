package br.com.luisbottino.exception

class InvalidApiKeyException :
    InfrastructureException(
        errorCode = ErrorCode.INVALID_API_KEY,
        message = "Invalid or missing API access key for the exchange rate service."
    )
