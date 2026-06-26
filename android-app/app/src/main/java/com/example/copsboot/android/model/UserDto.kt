package com.example.copsboot.android.model

data class UserDto(
    val id: UserIdDto?,
    val email: String?,
    val roles: List<String>?
)

data class UserIdDto(
    val id: String?,
    val value: String
)