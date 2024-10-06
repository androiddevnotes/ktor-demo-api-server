package com.example.quotes

import com.example.common.utils.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.quoteRoutes(quoteService: QuoteService, imageUploadService: ImageUploadService) {
    route("/quotes") {
        get {
            println("Received GET request for all quotes")
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            val quotesResponse = quoteService.getAllQuotes(page, pageSize)
            call.respond(quotesResponse)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
                "Invalid ID",
                status = HttpStatusCode.BadRequest
            )
            val quote = quoteService.getQuoteById(id) ?: return@get call.respondText(
                "Quote not found",
                status = HttpStatusCode.NotFound
            )
            call.respond(quote)
        }

        post {
            println("Received POST request for creating a quote")
            try {
                val multipart = call.receiveMultipart()
                var content: String? = null
                var author: String? = null
                var category: String? = null
                var imageUrl: String? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            println("Received form item: ${part.name} = ${part.value}")
                            when (part.name) {
                                "content" -> content = part.value
                                "author" -> author = part.value
                                "category" -> category = part.value
                            }
                        }
                        is PartData.FileItem -> {
                            println("Received file item: ${part.name}")
                            if (part.name == "image") {
                                imageUrl = imageUploadService.saveImage(part)
                                println("Saved image, URL: $imageUrl")
                            }
                        }
                        else -> println("Received unknown part: ${part::class.simpleName}")
                    }
                    part.dispose()
                }

                if (content != null && author != null) {
                    val quote = Quote(0, content!!, author!!, imageUrl, category)
                    val createdQuote = quoteService.createQuote(quote)
                    println("Created quote: $createdQuote")
                    call.respond(HttpStatusCode.Created, createdQuote)
                } else {
                    println("Missing content or author")
                    call.respondError(
                        HttpStatusCode.BadRequest,
                        "Missing content or author",
                        "INVALID_INPUT",
                        mapOf(
                            "content" to (content ?: "missing"),
                            "author" to (author ?: "missing")
                        )
                    )
                }
            } catch (e: Exception) {
                println("Error creating quote: ${e.message}")
                e.printStackTrace()
                call.respondError(
                    HttpStatusCode.InternalServerError,
                    "Error creating quote",
                    "INTERNAL_SERVER_ERROR"
                )
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respondText(
                "Invalid ID",
                status = HttpStatusCode.BadRequest
            )
            val quote = call.receive<Quote>()
            if (quoteService.updateQuote(id, quote)) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText(
                "Invalid ID",
                status = HttpStatusCode.BadRequest
            )
            if (quoteService.deleteQuote(id)) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}