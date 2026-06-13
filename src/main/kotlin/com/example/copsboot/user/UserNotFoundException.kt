package com.example.copsboot.user

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(
    userId: UserId
): RuntimeException("Could not find user with id ${userId.asString()}")
