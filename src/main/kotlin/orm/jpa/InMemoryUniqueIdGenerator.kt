package com.example.copsboot.orm.jpa

import java.util.UUID

class InMemoryUniqueIdGenerator : UniqueIdGenerator<UUID> {

    private val previousIds = mutableSetOf<UUID>()

    @Synchronized
    override fun getNextUniqueId(): UUID {
        var id: UUID

        do {
            id = UUID.randomUUID()
        } while (previousIds.contains(id))

        previousIds.add(id)

        return id
    }
}