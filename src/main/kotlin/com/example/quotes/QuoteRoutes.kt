package com.example.quotes

import com.example.common.exceptions.*
import com.example.common.exceptions.NotFoundException
import com.example.common.utils.*
import io.github.smiley4.ktorswaggerui.dsl.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.quoteRoutes(quoteService: QuoteService, imageUploadService: ImageUploadService) {
    route("/quotes") {
        get {
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            val quotesResponse = quoteService.getAllQuotes(page, pageSize)
            call.respond(quotesResponse)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondError(HttpStatusCode.BadRequest, "Invalid ID", "INVALID_ID")
                return@get
            }
            val quote = quoteService.getQuoteById(id)
            if (quote != null) {
                call.respond(quote)
            } else {
                call.respondError(HttpStatusCode.NotFound, "Quote not found", "QUOTE_NOT_FOUND")
            }
        }

        post {
            try {
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
            } catch (e: BadRequestException) {
                call.respondError(
                    HttpStatusCode.BadRequest,
                    e.message ?: "Invalid file",
                    "INVALID_FILE"
                )
            }
        }

        
    }
}