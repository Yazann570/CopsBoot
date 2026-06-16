package com.example.copsboot.report.web

import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.time.ZonedDateTime

class ReportDescriptionValidatorTest {

    // This test proves that an empty description is invalid because it does not contain "suspect".
    @Test
    fun givenEmptyStringNotValid() {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator

        val parameters = CreateReportParameters(
            dateTime = ZonedDateTime.now(),
            description = "",
            image = createMockImage()
        )

        val violations = validator.validate(parameters)

        assertThat(violations.map { it.propertyPath.toString() })
            .contains("description")
    }

    // This test proves that the description is valid when it contains "suspect".
    @Test
    fun givenSuspectWordPresentValid() {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator

        val parameters = CreateReportParameters(
            dateTime = ZonedDateTime.now(),
            description = "The suspect was wearing a black hat.",
            image = createMockImage()
        )

        val violations = validator.validate(parameters)

        assertThat(violations).isEmpty()
    }

    // This creates a valid fake image so the image @NotNull validator does not fail.
    private fun createMockImage(): MockMultipartFile {
        return MockMultipartFile(
            "image",
            "picture.png",
            "image/png",
            byteArrayOf(1, 2, 3)
        )
    }
}