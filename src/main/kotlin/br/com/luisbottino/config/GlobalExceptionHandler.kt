package br.com.luisbottino.config

import br.com.luisbottino.controller.v1.ApiErrorResponse
import br.com.luisbottino.exception.BusinessException
import br.com.luisbottino.exception.ErrorCode
import br.com.luisbottino.exception.InfrastructureException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        val message = ex.constraintViolations.firstOrNull()?.message ?: "Validation error"

        return buildResponse(ErrorCode.VALIDATION_ERROR, request, message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        val fieldErrors = ex.bindingResult.fieldErrors.map { fieldError ->
            "${fieldError.field}: ${fieldError.defaultMessage}"
        }
        return buildResponse(ErrorCode.VALIDATION_ERROR, request, fieldErrors)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        ex: BusinessException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return buildResponse(ex.errorCode, request, ex.message)
    }

    @ExceptionHandler(InfrastructureException::class)
    fun handleInfrastructureException(
        ex: InfrastructureException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return buildResponse(ex.errorCode, request, ex.message)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(
        ex: NoHandlerFoundException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return buildResponse(
            ErrorCode.RESOURCE_NOT_FOUND,
            request,
            "The requested endpoint '${ex.requestURL}' was not found."
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        val message = ex.message ?: "Unexpected error occurred"
        return buildResponse(ErrorCode.UNKNOWN_ERROR, request, message)
    }

    fun buildResponse(
        errorCode: ErrorCode,
        request: WebRequest,
        message: Any,
    ): ResponseEntity<ApiErrorResponse> {
        val path  = (request as? ServletWebRequest)?.request?.requestURI ?: "unknown"
        val error = ApiErrorResponse(
            status = errorCode.httpStatus.value(),
            error = errorCode.httpStatus.reasonPhrase,
            code = errorCode.code,
            message = message,
            path = path
        )

        return ResponseEntity(error, errorCode.httpStatus)
    }

}
