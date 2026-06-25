package com.example.copsboot.android.ui.login

import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.copsboot.android.api.ApiClient
import kotlinx.coroutines.launch


private const val CLIENT_ID = "copsboot-mobile-client"
private const val CLIENT_SECRET = "ccUyb6vS4S8nxfbKPCrN"
private const val SCOPE = "mobile_app"

class LoginViewModel : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set
    fun onEmailChanged(newEmail: String){
        uiState = uiState.copy(email = newEmail)
    }
    fun onPasswordChanged(newPassword: String){
        uiState = uiState.copy(password = newPassword)
    }
    fun fillTestOfficer(){
        uiState = uiState.copy(
            email = "officer@example.com",
            password = "officer",
            message = ""
        )
    }
    fun login(){
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                message = ""
            )

            try{
                val loginResponse = ApiClient.authApi.login(
                    authorization = createBasicAuthHeader(),
                    grantType = "password",
                    username = uiState.email,
                    password = uiState.password,
                    scope = SCOPE
                )
                if(loginResponse.isSuccessful){
                    val tokenResponse = loginResponse.body()
                    val token = tokenResponse?.accessToken
                    val tokenType = tokenResponse?.tokenType ?: "Bearer"

                    if(token.isNullOrBlank()){
                        uiState = uiState.copy(
                            message = "Login succeeded, but no access token was returned."
                        )
                    }else{
                        val authorizationHeader = "$tokenType $token"
                        val currentUserResponse = ApiClient.userApi.getCurrentUser(
                            authorizaton = authorizationHeader
                        )

                        uiState = if(currentUserResponse.isSuccessful){
                            val currentUserJson = currentUserResponse.body()?.string()
                            uiState.copy(
                                message = "Login successful.\n\nCurrent user:\n$currentUserJson"
                            )
                        }else{
                            uiState.copy(
                                message = "Login succeeded. but /api/users/me failed. Status code: ${currentUserResponse.code()}"
                            )
                        }
                    }
                }else{
                    uiState = uiState.copy(
                        message = "Login failed. Status code: ${loginResponse.code()}"
                    )
                }
            }catch(exception: Exception){
                uiState = uiState.copy(
                    message = "Network error: ${exception.message}"
                )
            }finally{
                uiState = uiState.copy(
                    isLoading = false
                )
            }

        }
    }
}
private fun createBasicAuthHeader(): String {
    val credentials = "$CLIENT_ID:$CLIENT_SECRET"

    val encodedCredentials = Base64.encodeToString(
        credentials.toByteArray(Charsets.UTF_8),
        Base64.NO_WRAP
    )

    return "Basic $encodedCredentials"
}