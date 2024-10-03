package com.example.routes

import com.example.models.*
import com.example.services.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.quoteRoutes(quoteService: QuoteService) {
    route("/quotes") {
        get {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10

            val quotes = quoteService.getAllQuotes(page, pageSize)
            val totalQuotes = quoteService.getTotalQuotes()
            val totalPages = (totalQuotes + pageSize - 1) / pageSize

            call.respond(
                HttpStatusCode.OK,
                mapOf(
                    "quotes" to quotes,
                    "page" to page,
                    "pageSize" to pageSize,
                    "totalQuotes" to totalQuotes,
                    "totalPages" to totalPages
                )
            )
        }

        get("/{id}") {

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val quote = quoteService.getQuoteById(id)
            if (quote != null) {
                call.respond(quote)
            } else {
                call.respond(HttpStatusCode.NotFound, "Quote not found")
            }
        }

        authenticate {


            post {
                val principal = call.principal<JWTPrincipal>()
                val role = principal!!.payload.getClaim("role").asString()
                if (role != "ADMIN") {
                    call.respond(HttpStatusCode.Forbidden, "Only admins can create quotes")
                    return@post
                }

                val quote = call.receive<Quote>()
                val createdQuote = quoteService.createQuote(quote)
                call.respond(HttpStatusCode.Created, createdQuote)
            }

            put("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val role = principal!!.payload.getClaim("role").asString()
                if (role != "ADMIN") {
                    call.respond(HttpStatusCode.Forbidden, "Only admins can update quotes")
                    return@put
                }

                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@put
                }

                val updatedQuote = call.receive<Quote>()
                if (quoteService.updateQuote(id, updatedQuote)) {
                    call.respond(HttpStatusCode.OK, "Quote updated successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Quote not found")
                }
            }

            delete("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val role = principal!!.payload.getClaim("role").asString()
                if (role != "ADMIN") {
                    call.respond(HttpStatusCode.Forbidden, "Only admins can delete quotes")
                    return@delete
                }

                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@delete
                }

                if (quoteService.deleteQuote(id)) {
                    call.respond(HttpStatusCode.OK, "Quote deleted successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Quote not found")
                }
            }
        }
    }
}