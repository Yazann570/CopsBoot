package com.example.copsboot.android.data.auth

import android.util.Base64
import com.example.copsboot.android.api.ApiClient

private const val CLIENT_ID = "copsboot-mobile-client"
private const val CLIENT_SECRET = "ccUyb6vS4S8nxfbKPCrN"
private const val SCOPE = "mobile_app"

class AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): AuthResult {
        return try {
            val loginResponse = ApiClient.authApi.login(
                authorization = createBasicAuthHeader(),
                grantType = "password",
                username = email,
                password = password,
                scope = SCOPE
            )

            if (!loginResponse.isSuccessful) {
                return AuthResult.Error(
                    message = "Login failed. Status code: ${loginResponse.code()}"
                )
            }

            val tokenResponse = loginResponse.body()
            val token = tokenResponse?.accessToken
            val tokenType = tokenResponse?.tokenType ?: "Bearer"

            if (token.isNullOrBlank()) {
                return AuthResult.Error(
                    message = "Login succeeded, but no access token was returned."
                )
            }

            val currentUserResponse = ApiClient.userApi.getCurrentUser(
                authorization = "$tokenType $token"
            )

            if (!currentUserResponse.isSuccessful) {
                return AuthResult.Error(
                    message = "Login succeeded, but /api/users/me failed. Status code: ${currentUserResponse.code()}"
                )
            }

            val currentUser = currentUserResponse.body()
            AuthSession.saveSession(
                accessToken = token,
                tokenType = tokenType,
                currentUser = currentUser
            )
            AuthResult.Success(
                accessToken = token,
                currentUser = currentUserResponse.body()
            )
        } catch (exception: Exception) {
            AuthResult.Error(
                message = "Network error: ${exception.message}"
            )
        }
    }

    fun logout(){
        AuthSession.clearSession()
    }
    private fun createBasicAuthHeader(): String {
        val credentials = "$CLIENT_ID:$CLIENT_SECRET"

        val encodedCredentials = Base64.encodeToString(
            credentials.toByteArray(Charsets.UTF_8),
            Base64.NO_WRAP
        )

        return "Basic $encodedCredentials"
    }
}