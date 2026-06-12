package com.example.copsboot

import com.example.copsboot.orm.jpa.UniqueIdGenerator
import com.example.copsboot.orm.jpa.InMemoryUniqueIdGenerator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

@SpringBootApplication
class CopsbootApplication{

	@Bean
	fun uniqueIdGenerator(): UniqueIdGenerator<UUID>{
		return InMemoryUniqueIdGenerator()
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder()
	}
}

fun main(args: Array<String>) {
	runApplication<CopsbootApplication>(*args)
}
