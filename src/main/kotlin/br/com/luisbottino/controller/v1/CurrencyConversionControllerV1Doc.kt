package br.com.luisbottino.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Slice

@Tag(name = "Currency Conversion", description = "Endpoints to manage currency conversion operations")
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
        @Parameter(
            description = "The user ID whose conversion history is to be fetched",
            example = "12345",
            required = true
        )
        userId: String,

        @Parameter(
            description = "Page number for pagination (starting from 0)",
            schema = Schema(defaultValue = "0", minimum = "0")
        )
        page: Int,

        @Parameter(
            description = "Page size specifying the maximum number of items per page",
            schema = Schema(defaultValue = "10", minimum = "1")
        )
        size: Int,
    ): Slice<ConversionHistoryResponse>

}