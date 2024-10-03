package com.example.repositories

import com.example.models.Quote
import com.example.models.Quotes
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class QuoteRepository {
    fun create(quote: Quote): Quote = transaction {
        val id = Quotes.insert {
            it[content] = quote.content
            it[author] = quote.author
        } get Quotes.id
        quote.copy(id = id)
    }

    fun getAll(): List<Quote> = transaction {
        Quotes.selectAll().map { toQuote(it) }
    }

    fun getById(id: Int): Quote? = transaction {
        Quotes.select { Quotes.id eq id }
            .map { toQuote(it) }
            .singleOrNull()
    }

    fun update(id: Int, quote: Quote): Boolean = transaction {
        val rowsUpdated = Quotes.update({ Quotes.id eq id }) {
            it[content] = quote.content
            it[author] = quote.author
        }
        rowsUpdated > 0
    }

    fun delete(id: Int): Boolean = transaction {
        val rowsDeleted = Quotes.deleteWhere { Quotes.id eq id }
        rowsDeleted > 0
    }

    private fun toQuote(row: ResultRow): Quote =
        Quote(row[Quotes.id], row[Quotes.content], row[Quotes.author])
}