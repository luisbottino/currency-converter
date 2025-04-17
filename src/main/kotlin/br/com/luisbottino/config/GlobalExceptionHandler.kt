package br.com.luisbottino.config

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: WebRequest
    ): ResponseEntity<Map<String, Any>> {
        val violationMessage = ex.constraintViolations.firstOrNull()?.message ?: "Validation error"
        val path = (request as ServletWebRequest).request.requestURI

        val errorDetails = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Bad Request",
            "message" to violationMessage,
            "path" to path
        )

        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<Map<String, Any>> {
        val fieldErrors = ex.bindingResult.fieldErrors.map { fieldError ->
            "${fieldError.field}: ${fieldError.defaultMessage}"
        }
        val path = (request as ServletWebRequest).request.requestURI

        val errorDetails = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Bad Request",
            "message" to fieldErrors,
            "path" to path
        )

        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

}
