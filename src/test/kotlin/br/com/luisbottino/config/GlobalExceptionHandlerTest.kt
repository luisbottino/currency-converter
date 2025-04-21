package br.com.luisbottino.config

import br.com.luisbottino.exception.BusinessException
import br.com.luisbottino.exception.ErrorCode
import br.com.luisbottino.exception.InfrastructureException
import io.mockk.every
import io.mockk.mockk
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.assertj.core.api.Assertions.assertThat
import org.springframework.core.MethodParameter
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.servlet.NoHandlerFoundException
import java.lang.reflect.Method


class GlobalExceptionHandlerTest {
  private lateinit var handler: GlobalExceptionHandler
  private lateinit var request: WebRequest

  @BeforeEach
  fun setUp() {
   val servletRequest = MockHttpServletRequest("GET", "/api/v1/conversions")
   request = ServletWebRequest(servletRequest)
   handler = GlobalExceptionHandler()
  }

  @Test
  fun givenValidBusinessException_whenHandleBusinessException_thenReturnExpectedResponse() {
   val exception = BusinessException(ErrorCode.CURRENCY_RATE_NOT_FOUND, "Conversion error")

   val response = handler.handleBusinessException(exception, request)

   assertThat(response.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
   assertThat(response.body?.code).isEqualTo("BUS001")
   assertThat(response.body?.message).isEqualTo("Conversion error")
  }

 @Test
 fun givenConstraintViolation_whenHandleConstraintViolationException_thenReturnValidationError() {
  val violation = mockk<ConstraintViolation<*>>()
  every { violation.message } returns "Invalid input"
  every { violation.propertyPath } returns mockk() // <- mocka o caminho da propriedade

  val exception = ConstraintViolationException(setOf(violation))

  val response = handler.handleConstraintViolationException(exception, request)

  assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
  assertThat(response.body?.code).isEqualTo("VAL001")
  assertThat(response.body?.message).isEqualTo("Invalid input")
 }

 @Test
 fun givenFieldErrors_whenHandleMethodArgumentNotValidException_thenReturnValidationErrors() {
  val bindingResult = BeanPropertyBindingResult(null, "request")
  bindingResult.addError(FieldError("request", "userId", "must not be blank"))

  // Criando um MethodParameter mockado para evitar ambiguidade
  val method: Method = this::class.java.getDeclaredMethod(
   "givenFieldErrors_whenHandleMethodArgumentNotValidException_thenReturnValidationErrors"
  )
  val parameter = MethodParameter(method, -1)

  val exception = MethodArgumentNotValidException(parameter, bindingResult)

  val response = handler.handleMethodArgumentNotValidException(exception, request)

  assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
  assertThat(response.body?.code).isEqualTo("VAL001")
  assertThat(response.body?.message.toString()).contains("userId")
 }

  @Test
  fun givenInfrastructureException_whenHandleInfrastructureException_thenReturnInternalServerError() {
   val exception = InfrastructureException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, "External API failure",)

   val response = handler.handleInfrastructureException(exception, request)

   assertThat(response.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
   assertThat(response.body?.code).isEqualTo("INF001")
   assertThat(response.body?.message).isEqualTo("External API failure")
  }

  @Test
  fun givenInvalidUrl_whenHandleNoHandlerFoundException_thenReturnNotFound() {
   val exception = NoHandlerFoundException("GET", "/not-found", null)

   val response = handler.handleNoHandlerFoundException(exception, request)

   assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
   assertThat(response.body?.code).isEqualTo("RES001")
   assertThat(response.body?.message.toString()).contains("/not-found")  }

  @Test
  fun givenUnhandledException_whenHandleGenericException_thenReturnGenericError() {
   val exception = RuntimeException("Unexpected crash")

   val response = handler.handleGenericException(exception, request)

   assertThat(response.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
   assertThat(response.body?.code).isEqualTo("GEN001")
   assertThat(response.body?.message).isEqualTo("Unexpected crash")
  }
 }
