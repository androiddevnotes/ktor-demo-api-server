package com.example

import com.example.config.*
import com.example.plugins.*
import com.example.repositories.*
import com.example.routes.*
import com.example.services.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

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
