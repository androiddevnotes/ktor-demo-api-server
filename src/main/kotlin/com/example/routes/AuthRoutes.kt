package com.example.routes

import com.example.config.JwtConfig
import com.example.models.UserDTO
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(userService: UserService) {
    post("/register") {
        val user = call.receive<UserDTO>()
        val registeredUser = userService.registerUser(user)
        call.respond(HttpStatusCode.Created, registeredUser)
    }

    post("/login") {
        val user = call.receive<UserDTO>()
        val validatedUser = userService.validateUser(user.username, user.password)
        if (validatedUser != null) {
            val token = JwtConfig.makeToken(validatedUser)
            call.respond(hashMapOf("token" to token))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
        }
    }
}