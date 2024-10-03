package com.example.services

import com.example.models.Quote
import com.example.repositories.QuoteRepository

class QuoteService(private val repository: QuoteRepository) {
    fun createQuote(quote: Quote): Quote = repository.create(quote)
    fun getAllQuotes(): List<Quote> = repository.getAll()
    fun getQuoteById(id: Int): Quote? = repository.getById(id)
    fun updateQuote(id: Int, quote: Quote): Boolean = repository.update(id, quote)
    fun deleteQuote(id: Int): Boolean = repository.delete(id)
}