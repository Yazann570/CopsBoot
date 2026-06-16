package com.example.copsboot.report.web

import com.example.copsboot.infrastructure.test.CopsbootControllerTest
import com.example.copsboot.report.Report
import com.example.copsboot.report.ReportId
import com.example.copsboot.report.ReportService
import com.example.copsboot.user.Users
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.ZonedDateTime
import java.util.UUID

@CopsbootControllerTest(ReportRestController::class)
class ReportRestControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockitoBean
    private lateinit var service: ReportService

    // This test proves that an authenticated officer can create a report with an image.
    @Test
    fun officerIsAbleToPostAReport() {
        val officer = Users.officer()
        val dateTime = "2018-04-11T22:59:03.189+02:00"
        val parsedDateTime = ZonedDateTime.parse(dateTime)
        val description = "The suspect is wearing a black hat."
        val image = createMockImage()

        `when`(
            service.createReport(
                officer.getId(),
                parsedDateTime,
                description,
                image
            )
        ).thenReturn(
            Report.create(
                ReportId(UUID.randomUUID()),
                officer,
                parsedDateTime,
                description
            )
        )

        mvc.perform(
            multipart("/api/reports")
                .file(image)
                .param("dateTime", dateTime)
                .param("description", description)
                .with(
                    jwt()
                        .jwt { jwt ->
                            jwt
                                .subject(Users.OFFICER_EMAIL)
                                .claim("user_id", officer.getId().asString())
                                .claim("roles", listOf("OFFICER"))
                        }
                )
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.reporter").value(Users.OFFICER_EMAIL))
            .andExpect(jsonPath("$.dateTime").value(dateTime))
            .andExpect(jsonPath("$.description").value(description))
    }

    // This creates a fake uploaded image for MockMvc.
    // The field name must be "image" because CreateReportParameters has an image property.
    private fun createMockImage(): MockMultipartFile {
        return MockMultipartFile(
            "image",
            "picture.png",
            "image/png",
            byteArrayOf(1, 2, 3)
        )
    }
}