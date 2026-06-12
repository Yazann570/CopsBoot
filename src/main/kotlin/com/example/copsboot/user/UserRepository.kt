package com.example.copsboot.user;

import org.springframework.data.repository.CrudRepository

import java.util.Optional

interface UserRepositoryCustom{
    fun nextId(): UserId
}

interface UserRepository : CrudRepository<User, UserId>,UserRepositoryCustom{
    fun findByEmailIgnoreCase(email: String): Optional<User>
}