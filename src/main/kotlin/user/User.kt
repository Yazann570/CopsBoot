package com.example.copsboot.user

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "copsboot_user")
class User(@Id val id: UUID, val email: String, val password: String, @ElementCollection(fetch= FetchType.EAGER) @Enumerated(EnumType.STRING) @field:NotNull val roles: Set<UserRole>)