package com.example.copsboot.user.web

import com.example.copsboot.infrastructure.SpringProfiles
import com.example.copsboot.user.UserService
import com.example.copsboot.user.Users
import tools.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Optional

@WebMvcTest(UserRestController::class)
@ActiveProfiles(SpringProfiles.TEST)
@Import(UserRestControllerTest.TestSecurityConfiguration::class)
class UserRestControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var service: UserService

    @Test
    fun givenNotAuthenticatedWhenAskingMyDetailsThenUnauthorized() {
        mvc.perform(get("/api/users/me"))
            .andExpect(status().isUnauthorized())
    }

    @Test
    fun givenAuthenticatedAsOfficerWhenAskingMyDetailsThenDetailsReturned() {
        val officer = Users.officer()

        `when`(service.getUser(officer.getId()))
            .thenReturn(Optional.of(officer))

        mvc.perform(
            get("/api/users/me")
                .with(
                    jwt()
                        .jwt { jwt ->
                            jwt
                                .subject(Users.OFFICER_EMAIL)
                                .claim("user_id", officer.getId().asString())
                                .claim("roles", listOf("OFFICER"))
                        }
                )
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.email").value(Users.OFFICER_EMAIL))
            .andExpect(jsonPath("$.roles").isArray())
            .andExpect(jsonPath("$.roles", hasItem("OFFICER")))
    }

    @Test
    fun testCreateOfficer() {
        val email = "wim.deblauwe@example.com"
        val password = "my-super-secret-pwd"
        val parameters = CreateOfficerParameters(
            email = email,
            password = password
        )

        `when`(service.createOfficer(email, password))
            .thenReturn(Users.newOfficer(email, password))

        mvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parameters))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.roles").isArray())
            .andExpect(jsonPath("$.roles", hasItem("OFFICER")))

        verify(service).createOfficer(email, password)
    }

    @TestConfiguration
    class TestSecurityConfiguration {

        @Bean
        fun testSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
            http
                .securityMatcher("/api/**")
                .csrf { csrf -> csrf.disable() }
                .authorizeHttpRequests { authorize ->
                    authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .anyRequest().authenticated()
                }
                .oauth2ResourceServer { resourceServer ->
                    resourceServer.jwt { }
                }

            return http.build()
        }

        @Bean
        fun jwtDecoder(): JwtDecoder {
            return org.mockito.Mockito.mock(JwtDecoder::class.java)
        }
    }
}

