package com.example.plugins

import com.example.models.Quote
import com.example.models.Quotes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun Application.configureRouting() {
    routing {
        // Create a new quote
        post("/quotes") {
            val quote = call.receive<Quote>()
            val id = transaction {
                Quotes.insert {
                    it[content] = quote.content
                    it[author] = quote.author
                } get Quotes.id
            }
            call.respond(HttpStatusCode.Created, Quote(id, quote.content, quote.author))
        }

        // Read all quotes
        get("/quotes") {
            val quotes = transaction {
                Quotes.selectAll().map { Quote(it[Quotes.id], it[Quotes.content], it[Quotes.author]) }
            }
            call.respond(quotes)
        }

        // Read a specific quote
        get("/quotes/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }
            
            val quote = transaction {
                Quotes.select { Quotes.id eq id }
                    .map { Quote(it[Quotes.id], it[Quotes.content], it[Quotes.author]) }
                    .singleOrNull()
            }
            
            if (quote != null) {
                call.respond(quote)
            } else {
                call.respond(HttpStatusCode.NotFound, "Quote not found")
            }
        }

        // Update a quote
        put("/quotes/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }
            
            val updatedQuote = call.receive<Quote>()
            val rowsUpdated = transaction {
                Quotes.update({ Quotes.id eq id }) {
                    it[content] = updatedQuote.content
                    it[author] = updatedQuote.author
                }
            }
            
            if (rowsUpdated > 0) {
                call.respond(HttpStatusCode.OK, "Quote updated successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Quote not found")
            }
        }

        // Delete a quote
        delete("/quotes/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }
            
            val rowsDeleted = transaction {
                Quotes.deleteWhere { Quotes.id eq id }
            }
            
            if (rowsDeleted > 0) {
                call.respond(HttpStatusCode.OK, "Quote deleted successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Quote not found")
            }
        }
    }
}
