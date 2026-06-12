package com.example.copsboot.infrastructure.security

import com.example.copsboot.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class ApplicationUserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService{
    override fun loadUserByUsername(username: String): UserDetails{
        val user = userRepository.findByEmailIgnoreCase(username)
            .orElseThrow{
                UsernameNotFoundException("User with email $username not found.")
            }
        return ApplicationUserDetails(user)
    }
}