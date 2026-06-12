package com.example.copsboot.infrastructure.security

import com.example.copsboot.infrastructure.SpringProfiles
import com.example.copsboot.user.UserService
import com.example.copsboot.user.Users
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(SpringProfiles.TEST)
class OAuth2ServerConfigurationTest @Autowired constructor(
    private val mvc: MockMvc,
    private val userService: UserService
) {

    @Test
    fun testGetAccessTokenAsOfficer() {
        userService.createOfficer(Users.OFFICER_EMAIL, Users.OFFICER_PASSWORD)

        val clientId = "test-client-id"
        val clientSecret = "test-client-secret"

        val params = LinkedMultiValueMap<String, String>()
        params.add("grant_type", "password")
        params.add("client_id", clientId)
        params.add("client_secret", clientSecret)
        params.add("username", Users.OFFICER_EMAIL)
        params.add("password", Users.OFFICER_PASSWORD)
        params.add("scope", OAuth2ServerConfiguration.MOBILE_APP_SCOPE)

        mvc.perform(
            post("/oauth/token")
                .params(params)
                .with(httpBasic(clientId, clientSecret))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.access_token").isString)
            .andExpect(jsonPath("$.token_type").value("Bearer"))
            .andExpect(jsonPath("$.refresh_token").isString)
            .andExpect(jsonPath("$.expires_in").isNumber)
            .andExpect(jsonPath("$.scope").value("mobile_app"))
    }
}