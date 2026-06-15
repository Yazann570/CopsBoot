package com.example.copsboot.user;

import org.springframework.data.jpa.repository.JpaRepository

import java.util.Optional

interface UserRepositoryCustom{
    fun nextId(): UserId
}

interface UserRepository : JpaRepository<User, UserId>,UserRepositoryCustom{
    fun findByEmailIgnoreCase(email: String): Optional<User>
    fun findByEmail(email: String): Optional<User>
}