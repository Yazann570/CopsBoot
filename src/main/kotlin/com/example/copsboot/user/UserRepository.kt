package com.example.copsboot.user;

import org.springframework.data.repository.CrudRepository

import java.util.UUID

interface UserRepositoryCustom{
    fun nextId(): UserId
}

interface UserRepository : CrudRepository<User, UUID>,UserRepositoryCustom