package com.example.copsboot.report

import com.example.orm.jpa.AbstractEntityId
import java.util.UUID

class ReportId : AbstractEntityId<UUID>{
    protected constructor(): super()
    constructor(id: UUID): super(id)
}