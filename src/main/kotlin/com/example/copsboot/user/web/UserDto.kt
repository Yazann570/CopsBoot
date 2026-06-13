package com.example.copsboot.user.web

import com.example.copsboot.user.User
import com.example.copsboot.user.UserId
import com.example.copsboot.user.UserRole

data class UserDto(
    val id: UserId,
    val email: String,
    val roles: Set<UserRole>
){
    companion object{
        fun fromUser(user: User): UserDto{
            return UserDto(
                id = user.getId(),
                email = user.email,
                roles = user.roles
            )
        }
    }
}