package com.example

import com.auth0.jwt.exceptions.*
import com.example.common.config.*
import com.example.common.exceptions.*
import com.example.common.exceptions.NotFoundException
import com.example.repositories.*
import com.example.routes.*
import com.example.services.*
import com.example.common.utils.*
import com.example.quotes.*
import io.github.smiley4.ktorswaggerui.*
import io.github.smiley4.ktorswaggerui.routing.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.*
import org.slf4j.event.*
import java.io.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseConfig.init(environment)
    val quoteRepository = QuoteRepository()
    val quoteService = QuoteService(quoteRepository)
    val userRepository = UserRepository()
    val userService = UserService(userRepository)
    val dictionaryRepository = DictionaryRepository()
    val dictionaryService = DictionaryService(dictionaryRepository)

    val uploadDir = environment.config.property("upload.dir").getString()
    val imageUploadService = ImageUploadService(uploadDir)

    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier())
            validate { credential ->
                try {
                    if (credential.payload.getClaim("username").asString() != "") {
                        JWTPrincipal(credential.payload)
                    } else {
                        throw AuthenticationFailureException("Invalid username claim")
                    }
                } catch (e: JWTVerificationException) {
                    throw UnauthorizedException("Invalid token")
                }
            }
            challenge { _, _ ->
                throw AuthenticationFailureException("Authentication failed")
            }
        }
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is IllegalArgumentException,
                is BadRequestException -> call.respondError(
                    HttpStatusCode.BadRequest,
                    cause.message ?: "Bad Request",
                    "BAD_REQUEST"
                )

                is NotFoundException -> call.respondError(
                    HttpStatusCode.NotFound,
                    cause.message ?: "Resource not found",
                    "NOT_FOUND"
                )

                is UnauthorizedException,
                is JWTVerificationException,
                is AuthenticationFailureException -> call.respondError(
                    HttpStatusCode.Unauthorized,
                    cause.message ?: "Unauthorized",
                    "UNAUTHORIZED"
                )

                is ForbiddenException -> call.respondError(
                    HttpStatusCode.Forbidden,
                    cause.message ?: "Forbidden",
                    "FORBIDDEN"
                )

                is ConflictException -> call.respondError(
                    HttpStatusCode.Conflict,
                    cause.message ?: "Conflict",
                    "CONFLICT"
                )

                else -> {
                    call.application.log.error("Unhandled exception", cause)
                    call.respondError(
                        HttpStatusCode.InternalServerError,
                        "An internal error occurred",
                        "INTERNAL_SERVER_ERROR"
                    )
                }
            }
        }
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respondError(
                HttpStatusCode.NotFound,
                "The requested resource was not found",
                "NOT_FOUND"
            )
        }
        status(HttpStatusCode.Unauthorized) { call, _ ->
            call.respondError(
                HttpStatusCode.Unauthorized,
                "Authentication required",
                "UNAUTHORIZED"
            )
        }
        status(HttpStatusCode.MethodNotAllowed) { call, _ ->
            call.respondError(
                HttpStatusCode.MethodNotAllowed,
                "The method is not allowed for the requested URL",
                "METHOD_NOT_ALLOWED"
            )
        }
        status(HttpStatusCode.UnsupportedMediaType) { call, _ ->
            call.respondError(
                HttpStatusCode.UnsupportedMediaType,
                "The server does not support the media type transmitted in the request",
                "UNSUPPORTED_MEDIA_TYPE"
            )
        }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            val path = call.request.path()
            val queryParams = call.request.queryParameters.entries()
                .joinToString(", ") { "${it.key}=${it.value}" }
            val duration = call.processingTimeMillis()
            val remoteHost = call.request.origin.remoteHost
            val coloredStatus = when {
                status == null -> "\u001B[33mUNKNOWN\u001B[0m"
                status.value < 300 -> "\u001B[32m$status\u001B[0m"
                status.value < 400 -> "\u001B[33m$status\u001B[0m"
                else -> "\u001B[31m$status\u001B[0m"
            }
            val coloredMethod = "\u001B[36m$httpMethod\u001B[0m"
            """
            |
            |------------------------ Request Details ------------------------
            |Status: $coloredStatus
            |Method: $coloredMethod
            |Path: $path
            |Query Params: $queryParams
            |Remote Host: $remoteHost
            |User Agent: $userAgent
            |Duration: ${duration}ms
            |------------------------------------------------------------------
            |
            """.trimMargin()
        }
    }

    install(SwaggerUI)

    configureRouting(quoteService, userService, imageUploadService, dictionaryService)
}

fun Application.configureRouting(
    quoteService: QuoteService,
    userService: UserService,
    imageUploadService: ImageUploadService,
    dictionaryService: DictionaryService
) {
    routing {
        route("swagger") {
            swaggerUI("/api.json")
        }
        route("api.json") {
            openApiSpec()
        }
        authRoutes(userService)
        quoteRoutes(quoteService, imageUploadService)
        dictionaryRoutes(dictionaryService)
        staticFiles("/images", File(environment?.config?.property("upload.dir")?.getString() ?: ""))
    }
}