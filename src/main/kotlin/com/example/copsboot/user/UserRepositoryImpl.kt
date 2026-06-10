package com.example.copsboot.user

import com.example.copsboot.orm.jpa.UniqueIdGenerator;
import java.util.UUID;

class UserRepositoryImpl(
    private val generator: UniqueIdGenerator<UUID>
): UserRepositoryCustom{
    override fun nextId(): UserId{
        return UserId(generator.getNextUniqueId())
    }
}