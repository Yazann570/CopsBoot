package com.example.copsboot.android.ui.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentUserJson: String = "",
    val accessToken: String = ""
)