package com.example.copsboot.report

import com.example.copsboot.user.User
import com.example.orm.jpa.AbstractEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "report")
class Report : AbstractEntity<ReportId> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    lateinit var reporter: User
        protected set

    lateinit var dateTime: ZonedDateTime
        protected set

    lateinit var description: String
        protected set

    // Required by JPA.
    // JPA creates entity objects using reflection, so it needs an empty constructor.
    protected constructor() : super()

    // Private constructor used internally by the companion object.
    // This keeps object creation controlled.
    private constructor(
        id: ReportId,
        reporter: User,
        dateTime: ZonedDateTime,
        description: String
    ) : super(id) {
        this.reporter = reporter
        this.dateTime = dateTime
        this.description = description
    }

    companion object {
        fun create(
            id: ReportId,
            reporter: User,
            dateTime: ZonedDateTime,
            description: String
        ): Report {
            return Report(
                id = id,
                reporter = reporter,
                dateTime = dateTime,
                description = description
            )
        }
    }
}