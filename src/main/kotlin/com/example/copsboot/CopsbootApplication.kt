package com.example.copsboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CopsbootApplication

fun main(args: Array<String>) {
	runApplication<CopsbootApplication>(*args)
}
