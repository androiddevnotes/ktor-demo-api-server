package com.example.repositories

import com.example.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.*

class QuoteRepository {
    fun create(quote: Quote): Quote = transaction {
        val id = Quotes.insert {
            it[content] = quote.content
            it[author] = quote.author
            it[imageUrl] = quote.imageUrl
            it[category] = quote.category
        } get Quotes.id
        quote.copy(id = id)
    }

    fun getAll(page: Int, pageSize: Int): List<Quote> = transaction {
        Quotes.selectAll()
            .orderBy(Quotes.id)
            .limit(pageSize, offset = ((page - 1) * pageSize).toLong())
            .map { toQuote(it) }
    }

    fun count(): Long = transaction {
        Quotes.selectAll().count()
    }

    fun getAll(): List<Quote> = transaction {
        Quotes.selectAll().map { toQuote(it) }
    }

    fun getById(id: Int): Quote? = transaction {
        Quotes.selectAll().where { Quotes.id eq id }
            .map { toQuote(it) }
            .singleOrNull()
    }

    fun update(id: Int, quote: Quote): Boolean = transaction {
        val rowsUpdated = Quotes.update({ Quotes.id eq id }) {
            it[content] = quote.content
            it[author] = quote.author
            it[imageUrl] = quote.imageUrl
            it[category] = quote.category
        }
        rowsUpdated > 0
    }

    fun delete(id: Int): Boolean = transaction {
        val rowsDeleted = Quotes.deleteWhere { Quotes.id eq id }
        rowsDeleted > 0
    }

    private fun toQuote(row: ResultRow): Quote =
        Quote(
            id = row[Quotes.id],
            content = row[Quotes.content],
            author = row[Quotes.author],
            imageUrl = row[Quotes.imageUrl],
            category = row[Quotes.category]
        )

    // Add a new method to get quotes by category
    fun getByCategory(category: String, page: Int, pageSize: Int): List<Quote> = transaction {
        Quotes.select { Quotes.category eq category }
            .orderBy(Quotes.id)
            .limit(pageSize, offset = ((page - 1) * pageSize).toLong())
            .map { toQuote(it) }
    }

    // Add a new method to count quotes by category
    fun countByCategory(category: String): Long = transaction {
        Quotes.select { Quotes.category eq category }.count()
    }
}