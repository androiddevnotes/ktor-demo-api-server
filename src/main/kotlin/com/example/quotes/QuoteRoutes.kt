package com.example.quotes

import com.example.common.utils.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

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
        val quoteData = receiveQuoteMultipart(call, imageUploadService)

        if (quoteData.content == null || quoteData.author == null) {
          call.respondError(HttpStatusCode.BadRequest, "Missing required fields", "MISSING_FIELDS")
          return@post
        }

        val quote =
          Quote(
            id = 0,
            content = quoteData.content,
            author = quoteData.author,
            imageUrl = quoteData.imageUrl,
            category = quoteData.category,
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
      val quoteDTO = call.receive<QuoteDTO>()
      val quote =
        Quote(
          id = id,
          content = quoteDTO.content,
          author = quoteDTO.author,
          imageUrl = quoteDTO.imageUrl,
          category = quoteDTO.category,
        )
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

private suspend fun receiveQuoteMultipart(
  call: ApplicationCall,
  imageUploadService: ImageUploadService,
): QuoteMultipartData {
  val multipart = call.receiveMultipart()
  var content: String? = null
  var author: String? = null
  var category: String? = null
  var imageUrl: String? = null

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
        if (part.name == "image") {
          imageUrl = imageUploadService.saveImage(part)
        }
      }
      else -> {}
    }
    part.dispose()
  }

  return QuoteMultipartData(content, author, category, imageUrl)
}

data class QuoteMultipartData(
  val content: String?,
  val author: String?,
  val category: String?,
  val imageUrl: String?,
)

@Serializable
data class QuoteDTO(
  val content: String,
  val author: String,
  val imageUrl: String? = null,
  val category: String? = null,
)
