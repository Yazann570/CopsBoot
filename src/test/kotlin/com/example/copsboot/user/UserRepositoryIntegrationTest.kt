package com.example.copsboot.user

import com.example.copsboot.infrastructure.SpringProfiles
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.junit.jupiter.api.Disabled

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles(SpringProfiles.INTEGRATION_TEST)
@Disabled("Requires PostgreSQL or Docker. Disabled for now because Docker is not installed.")
class UserRepositoryIntegrationTest {

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    // This test proves that our PostgreSQL Flyway migration can store a User entity correctly.
    // It saves a user through JPA, flushes the changes, then checks the real SQL tables.
    @Test
    fun testSaveUser() {
        val user = repository.save(
            User.createOfficer(
                repository.nextId(),
                "alex.foley@beverly-hills.com",
                "my-secret-pwd"
            )
        )

        assertThat(user).isNotNull()
        assertThat(repository.count()).isEqualTo(1L)

        entityManager.flush()

        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT count(*) FROM copsboot_user",
                Long::class.java
            )
        ).isEqualTo(1L)

        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT count(*) FROM user_roles",
                Long::class.java
            )
        ).isEqualTo(1L)

        assertThat(
            jdbcTemplate.queryForObject(
                "SELECT roles FROM user_roles",
                String::class.java
            )
        ).isEqualTo("OFFICER")
    }
}