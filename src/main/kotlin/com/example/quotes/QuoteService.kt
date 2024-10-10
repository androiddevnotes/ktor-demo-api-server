package com.example.quotes

import kotlinx.serialization.*

@Serializable
data class QuotesResponse(
  val quotes: List<Quote>,
  val page: Int,
  val pageSize: Int,
  val totalQuotes: Long,
  val totalPages: Int,
)

class QuoteService(
  private val repository: QuoteRepository,
) {
  fun createQuote(quote: Quote): Quote {
    val quoteWithCategory = quote.copy(category = quote.category ?: "Uncategorized")
    return repository.create(quoteWithCategory)
  }

  fun getAllQuotes(
    page: Int,
    pageSize: Int,
  ): QuotesResponse {
    val quotes = repository.getAll(page, pageSize)
    val totalQuotes = repository.count()
    val totalPages = (totalQuotes + pageSize - 1) / pageSize
    return QuotesResponse(
      quotes = quotes,
      page = page,
      pageSize = pageSize,
      totalQuotes = totalQuotes,
      totalPages = totalPages.toInt(),
    )
  }

  fun getQuoteById(id: Int): Quote? = repository.getById(id)

  fun updateQuote(
    id: Int,
    quote: Quote,
  ): Boolean {
    val quoteWithCategory = quote.copy(category = quote.category ?: "Uncategorized")
    return repository.update(id, quoteWithCategory)
  }

  fun deleteQuote(id: Int): Boolean = repository.delete(id)

  fun searchQuotes(query: String): List<Quote> = repository.search(query)
}
