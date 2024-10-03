package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureRouting()
    
    // You can add this line to log whether the application is running in development mode
    log.info("Development mode: ${environment.developmentMode}")
    log.info("Server started on port ${environment.config.port}")
}
