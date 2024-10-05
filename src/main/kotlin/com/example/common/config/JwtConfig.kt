package com.example.common.config

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import com.example.user.*
import java.util.*

object JwtConfig {
    private lateinit var secret: String
    private lateinit var issuer: String
    private lateinit var audience: String
    private const val VALIDITY_IN_MS = 36_000_00 * 10

    fun initialize(secret: String, issuer: String, audience: String) {
        this.secret = secret
        this.issuer = issuer
        this.audience = audience
    }

    fun makeToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("id", user.id)
        .withClaim("username", user.username)
        .withClaim("role", user.role)
        .withExpiresAt(getExpiration())
        .sign(Algorithm.HMAC256(secret))

    private fun getExpiration() = Date(System.currentTimeMillis() + VALIDITY_IN_MS)

    fun verifier(): JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withIssuer(issuer)
        .build()
}