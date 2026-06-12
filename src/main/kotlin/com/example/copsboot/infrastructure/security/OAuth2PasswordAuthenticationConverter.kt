package com.example.copsboot.infrastructure.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.util.StringUtils

class OAuth2PasswordAuthenticationConverter: AuthenticationConverter{
    override fun convert(request: HttpServletRequest): Authentication?{
        val grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE)
        if(OAuth2PasswordAuthenticationToken.PASSWORD_GRANT_TYPE.value != grantType) return null
        val clientPrincipal = SecurityContextHolder.getContext().authentication
        val parameters = getParameters(request)
        val username = parameters.getFirst(OAuth2PasswordParameterNames.USERNAME)
        if (!StringUtils.hasText(username) || parameters[OAuth2PasswordParameterNames.USERNAME]?.size != 1) {
            throw OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST)
        }

        val password = parameters.getFirst(OAuth2PasswordParameterNames.PASSWORD)
        if (!StringUtils.hasText(password) || parameters[OAuth2PasswordParameterNames.PASSWORD]?.size != 1) {
            throw OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST)
        }

        val requestedScope = parameters.getFirst(OAuth2ParameterNames.SCOPE)
        val scopes = if (StringUtils.hasText(requestedScope)) {
            StringUtils.delimitedListToStringArray(requestedScope, " ").toSet()
        } else {
            emptySet()
        }

        val additionalParameters = linkedMapOf<String, Any>()
        parameters.forEach { (key, value) ->
            if (
                key != OAuth2ParameterNames.GRANT_TYPE &&
                key != OAuth2ParameterNames.CLIENT_ID &&
                key != OAuth2PasswordParameterNames.USERNAME &&
                key != OAuth2PasswordParameterNames.PASSWORD &&
                key != OAuth2ParameterNames.SCOPE
            ) {
                additionalParameters[key] = value[0]
            }
        }

        return OAuth2PasswordAuthenticationToken(
            username = username!!,
            password = password!!,
            clientPrincipal = clientPrincipal!!,
            scopes = scopes,
            additionalParameters = additionalParameters
        )
    }

    private fun getParameters(request: HttpServletRequest): MultiValueMap<String, String> {
        val parameterMap = request.parameterMap
        val parameters = LinkedMultiValueMap<String, String>(parameterMap.size)

        parameterMap.forEach { (key, values) ->
            values.forEach { value ->
                parameters.add(key, value)
            }
        }

        return parameters
    }
}