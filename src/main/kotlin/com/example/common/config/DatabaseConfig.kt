package com.example.common.config

import com.example.dictionary.*
import com.example.quotes.*
import com.example.user.*
import com.example.common.utils.DatabaseSeeder
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

object DatabaseConfig {
    fun init(jdbcURL: String, user: String, password: String) {
        val database = Database.connect(jdbcURL, user = user, password = password)

        transaction(database) {
            SchemaUtils.create(Quotes, Users, DictionaryEntries)
            
            // Use the new seeder utility
            DatabaseSeeder.seedIfEmpty()
        }
    }
}