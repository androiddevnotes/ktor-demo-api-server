package com.example.quotes

import com.example.common.utils.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.quoteRoutes(
  quoteService: QuoteService,
  imageUploadService: ImageUploadService,
) {
  route("/quotes") {
    get {
      println("Received GET request for all quotes")
      val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
      val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
      val quotesResponse = quoteService.getAllQuotes(page, pageSize)
      call.respond(quotesResponse)
    }

    get("/{id}") {
      val id =
        call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
          "Invalid ID",
          status = HttpStatusCode.BadRequest,
        )
      val quote =
        quoteService.getQuoteById(id) ?: return@get call.respondText(
          "Quote not found",
          status = HttpStatusCode.NotFound,
        )
      call.respond(quote)
    }

    post {
      println("Received POST request for creating a quote")
      try {
        val quoteRequest = call.receive<QuoteRequest>()
        val quote =
          Quote(
            id = 0,
            content = quoteRequest.content,
            author = quoteRequest.author,
            imageUrl = quoteRequest.imageUrl,
            category = quoteRequest.category,
          )
        val createdQuote = quoteService.createQuote(quote)
        println("Created quote: $createdQuote")
        call.respond(HttpStatusCode.Created, createdQuote)
      } catch (e: Exception) {
        println("Error creating quote: ${e.message}")
        e.printStackTrace()
        call.respondError(
          HttpStatusCode.InternalServerError,
          "Error creating quote",
          "INTERNAL_SERVER_ERROR",
        )
      }
    }

    put("/{id}") {
      val id =
        call.parameters["id"]?.toIntOrNull() ?: return@put call.respondText(
          "Invalid ID",
          status = HttpStatusCode.BadRequest,
        )
      val quote = call.receive<Quote>()
      if (quoteService.updateQuote(id, quote)) {
        call.respond(HttpStatusCode.OK)
      } else {
        call.respond(HttpStatusCode.NotFound)
      }
    }

    delete("/{id}") {
      val id =
        call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText(
          "Invalid ID",
          status = HttpStatusCode.BadRequest,
        )
      if (quoteService.deleteQuote(id)) {
        call.respond(HttpStatusCode.OK)
      } else {
        call.respond(HttpStatusCode.NotFound)
      }
    }

    get("/search") {
      val query = call.request.queryParameters["q"] ?: ""
      val searchResults = quoteService.searchQuotes(query)
      call.respond(searchResults)
    }
  }
}

@Serializable
data class QuoteRequest(
  val content: String,
  val author: String,
  val imageUrl: String? = null,
  val category: String? = null,
)
