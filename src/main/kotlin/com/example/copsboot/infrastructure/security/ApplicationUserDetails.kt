package com.example.copsboot.infrastructure.security

import com.example.copsboot.user.User
import com.example.copsboot.user.UserId
import com.example.copsboot.user.UserRole
import org.springframework.security.core.authority.SimpleGrantedAuthority

class ApplicationUserDetails(
    user: User
): org.springframework.security.core.userdetails.User(
    user.email,
    user.password,
    createAuthorities(user.roles)
){
    val userId: UserId = user.getId()
    companion object{
        private const val ROLE_PREFIX = "ROLE_"
        private fun createAuthorities(roles: Set<UserRole>): Set<SimpleGrantedAuthority>{
            return roles.map{role -> SimpleGrantedAuthority(ROLE_PREFIX + role.name)}.toSet()
        }
    }
}