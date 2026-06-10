package com.example.orm.jpa

import jakarta.persistence.MappedSuperclass
import java.io.Serializable
import java.util.Objects

@MappedSuperclass //In order not to create a table for it.
abstract class AbstractEntityId<T: Serializable> protected constructor(): EntityId<T>{
    protected lateinit var idValue: T
    protected constructor(id: T): this(){
        this.idValue = Objects.requireNonNull(id)
    }
    override fun getId(): T{
        return idValue
    }
    override fun asString(): String{
        return idValue.toString()
    }
    override fun equals(other: Any?): Boolean{
        if(this === other) return true
        if(other !is AbstractEntityId<*>) return false
        return idValue == other.idValue
    }

    override fun hashCode(): Int{
        return idValue.hashCode();
    }
    override fun toString(): String{
        return "${this::class.simpleName}(id=$idValue)"
    }

}