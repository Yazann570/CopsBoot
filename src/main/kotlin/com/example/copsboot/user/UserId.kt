package com.example.copsboot.user

import com.example.orm.jpa.AbstractEntityId
import java.util.UUID

class UserId: AbstractEntityId<UUID>{
    protected constructor(): super()
    constructor(id: UUID): super(id)
}