package com.example.copsboot.infrastructure.security

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken

class OAuth2PasswordAuthenticationToken(
    val username: String,
    val password: String,
    clientPrincipal: Authentication,
    val scopes: Set<String>,
    additionalParameters: Map<String, Any>
): OAuth2AuthorizationGrantAuthenticationToken(
    PASSWORD_GRANT_TYPE,
    clientPrincipal,
    additionalParameters
){
    companion object{
        val PASSWORD_GRANT_TYPE = AuthorizationGrantType("password")
    }
}