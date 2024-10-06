package com.example

import com.auth0.jwt.exceptions.*
import com.example.common.config.*
import com.example.common.exceptions.*
import com.example.common.exceptions.NotFoundException
import com.example.common.utils.*
import com.example.dictionary.*
import com.example.quotes.*
import com.example.user.*
import io.github.cdimascio.dotenv.dotenv
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
import io.ktor.server.engine.*
import org.slf4j.LoggerFactory
import com.example.apikey.ApiKeyRepository
import io.ktor.server.response.*

private val logger = LoggerFactory.getLogger("com.example.Application")

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val dotenv = dotenv {
        ignoreIfMissing = true
    }

    
    System.setProperty("DATABASE_URL", dotenv["DATABASE_URL"] ?: "")
    System.setProperty("DATABASE_USER", dotenv["DATABASE_USER"] ?: "")
    System.setProperty("DATABASE_PASSWORD", dotenv["DATABASE_PASSWORD"] ?: "")

    DatabaseConfig.init()

    val jwtSecret = System.getenv("JWT_SECRET") ?: dotenv["JWT_SECRET"] ?: environment.config.property("jwt.secret").getString()
    val jwtIssuer = System.getenv("JWT_ISSUER") ?: dotenv["JWT_ISSUER"] ?: environment.config.property("jwt.issuer").getString()
    val jwtAudience = System.getenv("JWT_AUDIENCE") ?: dotenv["JWT_AUDIENCE"] ?: environment.config.property("jwt.audience").getString()
    val uploadDir = System.getenv("UPLOAD_DIR") ?: dotenv["UPLOAD_DIR"] ?: environment.config.propertyOrNull("upload.dir")?.getString() ?: "uploads"

    val quoteRepository = QuoteRepository()
    val quoteService = QuoteService(quoteRepository)
    val userRepository = UserRepository()
    val userService = UserService(userRepository)
    val dictionaryRepository = DictionaryRepository()
    val dictionaryService = DictionaryService(dictionaryRepository)
    val imageUploadService = ImageUploadService(uploadDir)

    JwtConfig.initialize(jwtSecret, jwtIssuer, jwtAudience)

    val apiKeyRepository = ApiKeyRepository()

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
        apiKeyAuth("apiKeyAuth") { apiKey ->
            apiKeyRepository.findByKey(apiKey)?.let { foundKey ->
                apiKeyRepository.updateLastUsed(foundKey.id)
                UserIdPrincipal(foundKey.userId.toString())
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

    configureRouting(quoteService, userService, imageUploadService, dictionaryService, apiKeyRepository)
}

fun Application.configureRouting(
    quoteService: QuoteService,
    userService: UserService,
    imageUploadService: ImageUploadService,
    dictionaryService: DictionaryService,
    apiKeyRepository: ApiKeyRepository
) {
    routing {
        route("swagger") {
            swaggerUI("/api.json")
        }
        route("api.json") {
            openApiSpec()
        }
        authRoutes(userService, apiKeyRepository)

        authenticate("apiKeyAuth") {
            route("/api/v1") {
                quoteRoutes(quoteService, imageUploadService)
                
                route("/terms") {
                    get {
                        call.respond(dictionaryService.getAllEntries())
                    }
                    
                }
            }
        }
        
        
        dictionaryRoutes(dictionaryService)
        
        val uploadDir = environment?.config?.propertyOrNull("upload.dir")?.getString() ?: "uploads"
        staticFiles("/images", File(uploadDir))
    }
}

fun AuthenticationConfig.apiKeyAuth(name: String, validate: suspend (String) -> Principal?) {
    register(object : AuthenticationProvider(object : AuthenticationProvider.Config(name) {}) {
        override suspend fun onAuthenticate(context: AuthenticationContext) {
            val call = context.call
            val apiKey = call.request.headers["X-API-Key"]
                ?: call.request.queryParameters["api_key"]
            if (apiKey == null) {
                context.challenge("APIKey", AuthenticationFailedCause.NoCredentials) { challenge, call ->
                    call.respondError(
                        HttpStatusCode.Unauthorized,
                        "Missing API Key",
                        "MISSING_API_KEY"
                    )
                    challenge.complete()
                }
                return
            }
            val principal = validate(apiKey)
            if (principal != null) {
                context.principal(principal)
            } else {
                context.challenge("APIKey", AuthenticationFailedCause.InvalidCredentials) { challenge, call ->
                    call.respondError(
                        HttpStatusCode.Unauthorized,
                        "Invalid API Key",
                        "INVALID_API_KEY"
                    )
                    challenge.complete()
                }
            }
        }
    })
}