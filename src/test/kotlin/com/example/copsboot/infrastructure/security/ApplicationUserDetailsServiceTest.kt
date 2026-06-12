package com.example.copsboot.infrastructure.security

import com.example.copsboot.user.UserRepository
import com.example.copsboot.user.Users
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.Optional

class ApplicationUserDetailsServiceTest {

    @Test
    fun givenExistingUsernameWhenLoadingUserThenUserIsReturned() {
        val repository = mock(UserRepository::class.java)
        val service = ApplicationUserDetailsService(repository)

        `when`(repository.findByEmailIgnoreCase(Users.OFFICER_EMAIL))
            .thenReturn(Optional.of(Users.officer()))

        val userDetails = service.loadUserByUsername(Users.OFFICER_EMAIL)

        assertThat(userDetails).isNotNull
        assertThat(userDetails.username).isEqualTo(Users.OFFICER_EMAIL)

        assertThat(userDetails.authorities)
            .extracting<String> { authority: GrantedAuthority -> authority.authority }
            .contains("ROLE_OFFICER")

        assertThat(userDetails).isInstanceOfSatisfying(
            ApplicationUserDetails::class.java
        ) { applicationUserDetails ->
            assertThat(applicationUserDetails.userId)
                .isEqualTo(Users.officer().getId())
        }
    }

    @Test
    fun givenNotExistingUsernameWhenLoadingUserThenExceptionThrown() {
        val repository = mock(UserRepository::class.java)
        val service = ApplicationUserDetailsService(repository)

        `when`(repository.findByEmailIgnoreCase(anyString()))
            .thenReturn(Optional.empty())

        assertThatThrownBy {
            service.loadUserByUsername("i@donotexist.com")
        }.isInstanceOf(UsernameNotFoundException::class.java)
    }
}