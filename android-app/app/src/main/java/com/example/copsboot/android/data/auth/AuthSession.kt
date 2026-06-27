package com.example.copsboot.android.data.auth

import com.example.copsboot.android.model.UserDto

object AuthSession{
    private var accessToken: String? = null
    private var tokenType: String? = "Bearer"

    var currentUser: UserDto? = null
        private set

    fun saveSession(
        accessToken: String,
        tokenType: String,
        currentUser: UserDto?
    ){
        this.accessToken = accessToken
        this.tokenType = tokenType
        this.currentUser = currentUser
    }
    fun getAuthorizationHeader(): String?{
        val token = accessToken ?: return null
        return "$tokenType $token"
    }
    fun clearSession(){
        accessToken = null
        tokenType = "Bearer"
        currentUser = null
    }
}