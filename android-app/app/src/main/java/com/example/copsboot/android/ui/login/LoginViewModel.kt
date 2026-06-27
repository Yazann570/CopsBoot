package com.example.copsboot.android.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.copsboot.android.data.auth.AuthRepository
import com.example.copsboot.android.data.auth.AuthResult
import com.example.copsboot.android.navigation.AppScreen
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChanged(newEmail: String) {
        uiState = uiState.copy(email = newEmail)
    }

    fun onPasswordChanged(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun fillTestOfficer() {
        uiState = uiState.copy(
            email = "officer@example.com",
            password = "officer",
            message = ""
        )
    }

    fun openCreateReport() {
        uiState = uiState.copy(
            currentScreen = AppScreen.CREATE_REPORT
        )
    }

    fun openHome() {
        uiState = uiState.copy(
            currentScreen = AppScreen.HOME
        )
    }

    fun logout() {
        authRepository.logout()
        uiState = LoginUiState()
    }

    fun login() {
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                message = ""
            )

            val result = authRepository.login(
                email = uiState.email,
                password = uiState.password
            )

            uiState = when (result) {
                is AuthResult.Success -> {
                    uiState.copy(
                        currentScreen = AppScreen.HOME,
                        accessToken = result.accessToken,
                        currentUser = result.currentUser,
                        message = "",
                        isLoading = false
                    )
                }

                is AuthResult.Error -> {
                    uiState.copy(
                        message = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}
