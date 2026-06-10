package com.example.orm.jpa;

interface Entity<T: EntityId<*>>{
    fun getId(): T
}