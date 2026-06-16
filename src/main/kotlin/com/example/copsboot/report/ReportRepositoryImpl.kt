package com.example.copsboot.report

import com.example.copsboot.orm.jpa.UniqueIdGenerator
import java.util.UUID

class ReportRepositoryImpl(
    private val generator: UniqueIdGenerator<UUID>
) : ReportRepositoryCustom {

    // Converts a generated UUID into a ReportId.
    override fun nextId(): ReportId {
        return ReportId(generator.getNextUniqueId())
    }
}