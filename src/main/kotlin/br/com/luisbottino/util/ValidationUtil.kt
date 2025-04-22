package br.com.luisbottino.util

fun <T : Any> requireField(value: T?, fieldName: String): T {
    return requireNotNull(value) { "$fieldName must not be null" }
}
