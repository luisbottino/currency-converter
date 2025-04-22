package br.com.luisbottino.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.math.BigDecimal
import java.math.RoundingMode

class BigDecimalTwoDigitsSerializer : JsonSerializer<BigDecimal>() {
    override fun serialize(
        value: BigDecimal,
        gen: JsonGenerator,
        serializers: SerializerProvider
    ) {
        gen.writeNumber(value.setScale(2, RoundingMode.HALF_UP))
    }
}
