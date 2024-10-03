package com.example.routes

import com.example.models.*
import com.example.services.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.quoteRoutes(quoteService: QuoteService) {
    route("/quotes") {
        post {
            val quote = call.receive<Quote>()
            val createdQuote = quoteService.createQuote(quote)
            call.respond(HttpStatusCode.Created, createdQuote)
        }

        get {
            val quotes = quoteService.getAllQuotes()
            call.respond(quotes)
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

        put("/{id}") {
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