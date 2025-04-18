package br.com.luisbottino.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Slice
import org.springframework.validation.annotation.Validated

@Tag(name = "Currency Conversion", description = "Endpoints to manage currency conversions operations")
@Validated
interface CurrencyConversionControllerV1Doc {

    @Operation(
        summary = "Retrieve the history of conversions for a user",
        description = "Returns a paginated history of conversions performed by a user based on their user ID.",
        responses = [
            ApiResponse(responseCode = "200", description = "Conversion history retrieved successfully."),
            ApiResponse(responseCode = "400", description = "Invalid request parameters."),
            ApiResponse(responseCode = "404", description = "User or conversion history not found."),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getConversionHistory(
        @Valid request: GetConversionHistoryRequest
    ): Slice<ConversionHistoryResponse>

}
