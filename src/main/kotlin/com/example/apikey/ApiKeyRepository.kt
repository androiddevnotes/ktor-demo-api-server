package com.example.apikey

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.*
import java.time.LocalDateTime
import java.util.*

class ApiKeyRepository {
    fun createApiKey(userId: Int): ApiKey = transaction {
        val key = generateApiKey()
        val id = ApiKeys.insert {
            it[ApiKeys.key] = key
            it[ApiKeys.userId] = userId
            it[createdAt] = LocalDateTime.now()
        } get ApiKeys.id

        ApiKey(id, key, userId, LocalDateTime.now().toString(), null)
    }

    fun findByKey(key: String): ApiKey? = transaction {
        ApiKeys.selectAll().where { ApiKeys.key eq key }
            .mapNotNull { toApiKey(it) }
            .singleOrNull()
    }

    fun updateLastUsed(id: Int) = transaction {
        ApiKeys.update({ ApiKeys.id eq id }) {
            it[lastUsedAt] = LocalDateTime.now()
        }
    }

    private fun toApiKey(row: ResultRow): ApiKey =
        ApiKey(
            id = row[ApiKeys.id],
            key = row[ApiKeys.key],
            userId = row[ApiKeys.userId],
            createdAt = row[ApiKeys.createdAt].toString(),
            lastUsedAt = row[ApiKeys.lastUsedAt]?.toString()
        )

    private fun generateApiKey(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}