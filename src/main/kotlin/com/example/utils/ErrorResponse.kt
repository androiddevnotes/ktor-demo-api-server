package com.example.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

data class ErrorResponse(
    val status: Int,
    val message: String,
    val code: String,
    val details: Map<String, Any>? = null
)

suspend fun ApplicationCall.respondError(
    status: HttpStatusCode,
    message: String,
    code: String,
    details: Map<String, Any>? = null
) {
    this.respond(status, ErrorResponse(status.value, message, code, details))
}