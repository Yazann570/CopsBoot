package com.example.orm.jpa;

import java.io.Serializable

interface EntityId<T: Serializable>: Serializable{
    fun getId(): T
    fun asString(): String
}