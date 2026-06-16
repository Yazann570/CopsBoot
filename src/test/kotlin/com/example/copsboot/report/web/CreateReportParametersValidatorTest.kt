package com.example.copsboot.report.web

import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.time.ZonedDateTime

class CreateReportParametersValidatorTest {

    // This test proves that a traffic incident with zero involved cars is invalid.
    @Test
    fun givenTrafficIncidentButInvolvedCarsZeroInvalid() {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator

        val parameters = CreateReportParameters(
            dateTime = ZonedDateTime.now(),
            description = "The suspect was wearing a black hat.",
            trafficIncident = true,
            numberOfInvolvedCars = 0,
            image = createMockImage()
        )

        val violations = validator.validate(parameters)

        assertThat(violations).isNotEmpty()
    }

    // This test proves that a traffic incident is valid when involved cars is positive.
    @Test
    fun givenTrafficIncidentInvolvedCarsMustBePositive() {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator

        val parameters = CreateReportParameters(
            dateTime = ZonedDateTime.now(),
            description = "The suspect was wearing a black hat.",
            trafficIncident = true,
            numberOfInvolvedCars = 2,
            image = createMockImage()
        )

        val violations = validator.validate(parameters)

        assertThat(violations).isEmpty()
    }

    // This test proves that involved cars does not matter when it is not a traffic incident.
    @Test
    fun givenNoTrafficIncidentInvolvedCarsDoesNotMatter() {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator

        val parameters = CreateReportParameters(
            dateTime = ZonedDateTime.now(),
            description = "The suspect was wearing a black hat.",
            trafficIncident = false,
            numberOfInvolvedCars = 0,
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