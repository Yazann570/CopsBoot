package com.example.copsboot.android.ui.login

import com.example.copsboot.android.model.UserDto
import com.example.copsboot.android.navigation.AppScreen

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
    val currentScreen: AppScreen = AppScreen.LOGIN,
    val currentUser: UserDto? = null,
    val accessToken: String = ""
)