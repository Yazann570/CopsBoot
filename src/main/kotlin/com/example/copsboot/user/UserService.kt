package com.example.copsboot.user

interface UserService{
    fun createOfficer(email: String, password: String): User
}