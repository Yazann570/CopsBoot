package com.example.copsboot.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun createOfficer(email: String, password: String): User {
        val encodedPassword = requireNotNull(passwordEncoder.encode(password))

        val user = User.createOfficer(
            repository.nextId(),
            email,
            encodedPassword
        )

        return repository.save(user)
    }
}