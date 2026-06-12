package com.example.copsboot.infrastructure.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.core.ClaimAccessor
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator
import java.security.Principal

class OAuth2PasswordAuthenticationProvider(
    private val authenticationManager: AuthenticationManager,
    private val authorizationService: OAuth2AuthorizationService,
    private val tokenGenerator: OAuth2TokenGenerator<OAuth2Token>
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val passwordAuthentication = authentication as OAuth2PasswordAuthenticationToken

        val clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(passwordAuthentication)

        val registeredClient = clientPrincipal.registeredClient
            ?: throw OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT)

        if (!registeredClient.authorizationGrantTypes.contains(OAuth2PasswordAuthenticationToken.PASSWORD_GRANT_TYPE)) {
            throw OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT)
        }

        val requestedScopes = passwordAuthentication.scopes
        val authorizedScopes = if (requestedScopes.isEmpty()) {
            registeredClient.scopes
        } else {
            if (!registeredClient.scopes.containsAll(requestedScopes)) {
                throw OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE)
            }

            requestedScopes
        }

        val userAuthentication = authenticateUser(passwordAuthentication)

        val accessTokenContext = DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(userAuthentication)
            .authorizationServerContext(AuthorizationServerContextHolder.getContext())
            .authorizedScopes(authorizedScopes)
            .authorizationGrantType(OAuth2PasswordAuthenticationToken.PASSWORD_GRANT_TYPE)
            .authorizationGrant(passwordAuthentication)
            .tokenType(OAuth2TokenType.ACCESS_TOKEN)
            .build()

        val generatedAccessToken = tokenGenerator.generate(accessTokenContext)
            ?: throw OAuth2AuthenticationException(
                OAuth2Error(
                    OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.",
                    null
                )
            )

        val accessToken = OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            generatedAccessToken.tokenValue,
            generatedAccessToken.issuedAt,
            generatedAccessToken.expiresAt,
            authorizedScopes
        )

        val authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
            .principalName(userAuthentication.name)
            .authorizationGrantType(OAuth2PasswordAuthenticationToken.PASSWORD_GRANT_TYPE)
            .authorizedScopes(authorizedScopes)
            .attribute(Principal::class.java.name, userAuthentication)
            .attribute(OAuth2PasswordParameterNames.USERNAME, passwordAuthentication.username)

        if (generatedAccessToken is ClaimAccessor) {
            authorizationBuilder.token(accessToken) { metadata ->
                metadata[OAuth2Authorization.Token.CLAIMS_METADATA_NAME] =
                    generatedAccessToken.claims
            }
        } else {
            authorizationBuilder.accessToken(accessToken)
        }

        val refreshToken = generateRefreshToken(
            registeredClient = registeredClient!!,
            userAuthentication = userAuthentication,
            passwordAuthentication = passwordAuthentication,
            authorizedScopes = authorizedScopes
        )

        if (refreshToken != null) {
            authorizationBuilder.refreshToken(refreshToken)
        }

        val authorization = authorizationBuilder.build()
        authorizationService.save(authorization)

        return OAuth2AccessTokenAuthenticationToken(
            registeredClient,
            clientPrincipal,
            accessToken,
            refreshToken
        )
    }

    override fun supports(authentication: Class<*>): Boolean {
        return OAuth2PasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    private fun authenticateUser(passwordAuthentication: OAuth2PasswordAuthenticationToken): Authentication {
        val usernamePasswordAuthentication = UsernamePasswordAuthenticationToken.unauthenticated(
            passwordAuthentication.username,
            passwordAuthentication.password
        )

        return try {
            authenticationManager.authenticate(usernamePasswordAuthentication)
        } catch (ex: AuthenticationException) {
            throw OAuth2AuthenticationException(
                OAuth2Error(
                    OAuth2ErrorCodes.INVALID_GRANT,
                    "Bad credentials",
                    null
                )
            )
        }
    }

    private fun generateRefreshToken(
        registeredClient: org.springframework.security.oauth2.server.authorization.client.RegisteredClient,
        userAuthentication: Authentication,
        passwordAuthentication: OAuth2PasswordAuthenticationToken,
        authorizedScopes: Set<String>
    ): OAuth2RefreshToken? {
        if (!registeredClient.authorizationGrantTypes.contains(org.springframework.security.oauth2.core.AuthorizationGrantType.REFRESH_TOKEN)) {
            return null
        }

        val refreshTokenContext = DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(userAuthentication)
            .authorizationServerContext(AuthorizationServerContextHolder.getContext())
            .authorizedScopes(authorizedScopes)
            .authorizationGrantType(OAuth2PasswordAuthenticationToken.PASSWORD_GRANT_TYPE)
            .authorizationGrant(passwordAuthentication)
            .tokenType(OAuth2TokenType.REFRESH_TOKEN)
            .build()

        val generatedRefreshToken = tokenGenerator.generate(refreshTokenContext)

        if (generatedRefreshToken !is OAuth2RefreshToken) {
            throw OAuth2AuthenticationException(
                OAuth2Error(
                    OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the refresh token.",
                    null
                )
            )
        }

        return generatedRefreshToken
    }

    private fun getAuthenticatedClientElseThrowInvalidClient(
        authentication: Authentication
    ): OAuth2ClientAuthenticationToken {
        val clientPrincipal = authentication.principal

        if (clientPrincipal is OAuth2ClientAuthenticationToken &&
            clientPrincipal.isAuthenticated &&
            clientPrincipal.registeredClient != null
        ) {
            return clientPrincipal
        }

        throw OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT)
    }
}