package com.example.copsboot.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserServiceImpl(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun createOfficer(email: String, password: String): User {
        val normalizedEmail = email.trim().lowercase()

        if (repository.findByEmail(normalizedEmail).isPresent) {
            throw IllegalArgumentException("User with email $normalizedEmail already exists")
        }

        val encodedPassword = requireNotNull(passwordEncoder.encode(password))

        val user = User.createOfficer(
            repository.nextId(),
            normalizedEmail,
            encodedPassword
        )

        return repository.save(user)
    }

    override fun getUser(userId: UserId): Optional<User>{
        return repository.findById(userId)
    }
    override fun findUserByEmail(email: String): Optional<User>{
        return repository.findByEmail(email)
    }
}