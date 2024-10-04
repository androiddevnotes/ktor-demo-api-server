package com.example.user

import org.mindrot.jbcrypt.BCrypt

class UserService(private val repository: UserRepository) {
    fun registerUser(user: UserDTO): User = repository.createUser(user)

    fun validateUser(username: String, password: String): User? {
        val user = repository.findByUsername(username)
        return if (user != null && BCrypt.checkpw(password, user.password)) {
            user
        } else {
            null
        }
    }
}