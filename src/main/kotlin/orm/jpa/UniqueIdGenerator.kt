package com.example.copsboot.orm.jpa

interface UniqueIdGenerator<T> {
    fun getNextUniqueId(): T
}