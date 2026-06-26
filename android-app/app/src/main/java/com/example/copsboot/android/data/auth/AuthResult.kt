package com.example.copsboot.android.data.auth

import com.example.copsboot.android.model.UserDto


sealed class AuthResult{
    data class Success(
        val accessToken: String,
        val currentUser: UserDto?,
    ): AuthResult()

    data class Error(
        val message: String
    ): AuthResult()
}