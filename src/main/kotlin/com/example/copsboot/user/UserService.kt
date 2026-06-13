package com.example.copsboot.user

import java.util.Optional

interface UserService{
    fun createOfficer(email: String, password: String): User
    fun getUser(userId: UserId) : Optional<User>
}