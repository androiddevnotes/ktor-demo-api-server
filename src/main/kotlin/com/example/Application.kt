package com.example

import com.example.config.DatabaseConfig
import com.example.repositories.QuoteRepository
import com.example.routes.quoteRoutes
import com.example.services.QuoteService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import com.example.plugins.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseConfig.init(environment)
    val quoteRepository = QuoteRepository()
    val quoteService = QuoteService(quoteRepository)

    configureSerialization()
    configureRouting(quoteService)
}

fun Application.configureRouting(quoteService: QuoteService) {
    routing {
        quoteRoutes(quoteService)
    }
}
