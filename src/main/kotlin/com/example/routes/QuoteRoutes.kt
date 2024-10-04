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
import io.ktor.http.content.*
import com.example.utils.respondError
import com.example.exceptions.*
import com.example.exceptions.NotFoundException
import io.github.smiley4.ktorswaggerui.dsl.routing.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.quoteRoutes(quoteService: QuoteService, imageUploadService: ImageUploadService) {
    authenticate {
        post("/quotes") {
            try {
                val multipart = call.receiveMultipart()
                var content: String? = null
                var author: String? = null
                var imageUrl: String? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "content" -> content = part.value
                                "author" -> author = part.value
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
                    val quote = Quote(0, content!!, author!!, imageUrl)
                    val createdQuote = quoteService.createQuote(quote)
                    call.respond(HttpStatusCode.Created, createdQuote)
                } else {
                    call.respondError(
                        HttpStatusCode.BadRequest,
                        "Missing content or author",
                        "INVALID_INPUT",
                        mapOf("content" to (content ?: "missing"), "author" to (author ?: "missing"))
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

    route("/quotes") {
        
        get({
            description = "Get all quotes with pagination"
        }) {
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

        
        get("/{id}", {
            description = "Get a specific quote by ID"
        }) {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid ID format")

            val quote = quoteService.getQuoteById(id)
                ?: throw NotFoundException("Quote not found")

            call.respond(quote)
        }

        authenticate {
            
            post({
                description = "Create a new quote (admin only)"
            }) {
                val principal = call.principal<JWTPrincipal>()
                val role = principal!!.payload.getClaim("role").asString()
                if (role != "ADMIN") {
                    throw ForbiddenException("Only admins can create quotes")
                }

                val quote = call.receive<Quote>()
                val createdQuote = quoteService.createQuote(quote)
                call.respond(HttpStatusCode.Created, createdQuote)
            }

            
            put("/{id}", {
                description = "Update an existing quote (admin only)"
            }) {
                val principal = call.principal<JWTPrincipal>()
                val role = principal!!.payload.getClaim("role").asString()
                if (role != "ADMIN") {
                    call.respondError(
                        HttpStatusCode.Forbidden,
                        "Only admins can update quotes",
                        "INSUFFICIENT_PERMISSIONS"
                    )
                    return@put
                }

                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respondError(
                        HttpStatusCode.BadRequest,
                        "Invalid ID format",
                        "INVALID_ID_FORMAT",
                        mapOf("id" to (call.parameters["id"] ?: "missing"))
                    )
                    return@put
                }

                val updatedQuote = call.receive<Quote>()
                if (quoteService.updateQuote(id, updatedQuote)) {
                    call.respond(HttpStatusCode.OK, "Quote updated successfully")
                } else {
                    call.respondError(
                        HttpStatusCode.NotFound,
                        "Quote not found",
                        "QUOTE_NOT_FOUND",
                        mapOf("id" to id)
                    )
                }
            }

            
            delete("/{id}", {
                description = "Delete a quote (admin only)"
            }) {
                val principal = call.principal<JWTPrincipal>()
                val role = principal!!.payload.getClaim("role").asString()
                if (role != "ADMIN") {
                    call.respondError(
                        HttpStatusCode.Forbidden,
                        "Only admins can delete quotes",
                        "INSUFFICIENT_PERMISSIONS"
                    )
                    return@delete
                }

                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respondError(
                        HttpStatusCode.BadRequest,
                        "Invalid ID format",
                        "INVALID_ID_FORMAT",
                        mapOf("id" to (call.parameters["id"] ?: "missing"))
                    )
                    return@delete
                }

                if (quoteService.deleteQuote(id)) {
                    call.respond(HttpStatusCode.OK, "Quote deleted successfully")
                } else {
                    call.respondError(
                        HttpStatusCode.NotFound,
                        "Quote not found",
                        "QUOTE_NOT_FOUND",
                        mapOf("id" to id)
                    )
                }
            }
        }
    }

    
}