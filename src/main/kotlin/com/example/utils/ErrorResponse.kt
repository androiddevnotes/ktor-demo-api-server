package com.example.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int,
    val message: String,
    val code: String,
    val details: Map<String, String>? = null
)

suspend fun ApplicationCall.respondError(
    status: HttpStatusCode,
    message: String,
    code: String,
    details: Map<String, Any>? = null
) {
    val stringDetails = details?.mapValues { it.value.toString() }
    this.respond(status, ErrorResponse(status.value, message, code, stringDetails))
}