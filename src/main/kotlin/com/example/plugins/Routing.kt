package com.example.plugins

import com.example.common.utils.*
import com.example.quotes.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import io.ktor.http.content.*

fun Application.configureRouting(quoteService: QuoteService, imageUploadService: ImageUploadService) {
    routing {
        post("/quotes") {
            val multipart = call.receiveMultipart()
            var content: String? = null
            var author: String? = null
            var imageUrl: String? = null
            var category: String? = null

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "content" -> content = part.value
                            "author" -> author = part.value
                            "category" -> category = part.value
                        }
                    }
                    is PartData.FileItem -> {
                        imageUrl = imageUploadService.saveImage(part)
                    }
                    else -> {}
                }
                part.dispose()
            }

            if (content != null && author != null) {
                val quote = Quote(0, content!!, author!!, imageUrl, category ?: "Uncategorized")
                val createdQuote = quoteService.createQuote(quote)
                call.respond(HttpStatusCode.Created, createdQuote)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing content or author")
            }
        }

        get("/quotes") {
            val quotes = quoteService.getAllQuotes()
            call.respond(quotes)
        }

        get("/quotes/{id}") {
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

        put("/quotes/{id}") {
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

        delete("/quotes/{id}") {
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

        get("/db-content") {
            val content = transaction {
                Quotes.selectAll().map {
                    "ID: ${it[Quotes.id]}, Content: ${it[Quotes.content]}, Author: ${it[Quotes.author]}, Category: ${it[Quotes.category]}"
                }
            }
            call.respondText(content.joinToString("\n"))
        }
    }
}
