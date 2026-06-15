package com.example.copsboot.infrastructure.test

import com.example.copsboot.infrastructure.SpringProfiles
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.AliasFor
import org.springframework.test.context.ActiveProfiles
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@WebMvcTest
@ActiveProfiles(SpringProfiles.TEST)
@Import(CopsbootControllerTestConfiguration::class)
annotation class CopsbootControllerTest(
    @get:AliasFor(annotation = WebMvcTest::class, attribute = "controllers")
    vararg val value: KClass<*>
)
