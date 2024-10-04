package com.example.quotes

class QuoteService(private val repository: QuoteRepository) {
    fun createQuote(quote: Quote): Quote = repository.create(quote.copy(category = quote.category ?: "Uncategorized"))
    fun getAllQuotes(): List<Quote> = repository.getAll()
    fun getQuoteById(id: Int): Quote? = repository.getById(id)
    fun updateQuote(id: Int, quote: Quote): Boolean = repository.update(id, quote)
    fun deleteQuote(id: Int): Boolean = repository.delete(id)
    
    fun getAllQuotes(page: Int, pageSize: Int): List<Quote> = repository.getAll(page, pageSize)
    fun getTotalQuotes(): Long = repository.count()
    
    fun getQuotesByCategory(category: String, page: Int, pageSize: Int): List<Quote> =
        repository.getByCategory(category, page, pageSize)

    fun getTotalQuotesByCategory(category: String): Long = repository.countByCategory(category)
}