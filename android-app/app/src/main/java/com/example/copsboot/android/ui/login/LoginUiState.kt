package com.example.copsboot.android.ui.login

import com.example.copsboot.android.model.UserDto

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentUser: UserDto? = null,
    val accessToken: String = ""
)