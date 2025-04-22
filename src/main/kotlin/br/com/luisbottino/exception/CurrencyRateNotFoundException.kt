package br.com.luisbottino.exception

open class CurrencyRateNotFoundException(from: String, to: String): BusinessException(
    errorCode = ErrorCode.CURRENCY_RATE_NOT_FOUND,
    message = "Conversion rate not found for $from to $to"
)
