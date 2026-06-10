package com.example.copsboot.user

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import java.util.UUID

@DataJpaTest
class UserRepositoryTest @Autowired constructor(private val repository: UserRepository){
    @Test
    fun testStoreUser(){
        val roles = hashSetOf(UserRole.OFFICER)
        val user = repository.save(
            User(
                repository.nextId(),
                "yazan@example.com",
                "Yazan123",
                roles
            )
        )
        assertThat(user).isNotNull
        assertThat(repository.count()).isEqualTo(1L)
    }
}