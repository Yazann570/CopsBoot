package com.example.copsboot.user

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import java.util.Locale

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

    @Test
    fun testFindByEmail(){
        val user = Users.newRandomOfficer()
        repository.save(user)
        val optional = repository.findByEmailIgnoreCase(user.email)
        assertThat(optional)
            .isNotEmpty().contains(user)
    }
    @Test
    fun testFindByEmailIgnoringCase() {
        val user = Users.newRandomOfficer()
        repository.save(user)

        val optional = repository.findByEmailIgnoreCase(
            user.email.uppercase(Locale.US)
        )

        assertThat(optional)
            .isNotEmpty
            .contains(user)
    }

    @Test
    fun testFindByEmailUnknownEmail() {
        val user = Users.newRandomOfficer()
        repository.save(user)

        val optional = repository.findByEmailIgnoreCase("will.not@find.me")

        assertThat(optional).isEmpty
    }
}