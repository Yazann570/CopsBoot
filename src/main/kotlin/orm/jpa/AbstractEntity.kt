package com.example.orm.jpa

import jakarta.persistence.EmbeddedId
import jakarta.persistence.MappedSuperclass
import java.util.Objects

@MappedSuperclass
abstract class AbstractEntity<T: EntityId<*>> protected constructor(): Entity<T>{
    @EmbeddedId
    private lateinit var idValue: T
    constructor(id: T): this(){
        this.idValue = Objects.requireNonNull(id)
    }
    override fun getId(): T{
        return idValue
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is AbstractEntity<*>) {
            return false
        }

        return getId() == other.getId()
    }

    override fun hashCode(): Int {
        return getId().hashCode()
    }

    override fun toString(): String {
        return "${this::class.simpleName}(id=${getId()})"
    }
}

