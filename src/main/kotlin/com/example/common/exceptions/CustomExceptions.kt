package com.example.common.exceptions

class NotFoundException(message: String) : RuntimeException(message)
class UnauthorizedException(message: String) : RuntimeException(message)
class ForbiddenException(message: String) : RuntimeException(message)
class ConflictException(message: String) : RuntimeException(message)
class AuthenticationFailureException(message: String) : RuntimeException(message)