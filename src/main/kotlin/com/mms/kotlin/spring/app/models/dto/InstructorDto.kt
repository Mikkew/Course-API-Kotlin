package com.mms.kotlin.spring.app.models.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.io.Serializable

data class InstructorDto (

    @field:JsonProperty(value = "id")
    var id: Long?,

    @field:JsonProperty(value = "name")
    @field:NotBlank(message = "Name is required")
    @field:NotEmpty(message = "Name is required")
    var name: String?
): Serializable {
    private constructor(builder: InstructorDto.Builder) : this(
        builder.id,
        builder.name
    )

    class Builder {
        var id: Long? = null
            private set
        var name: String? = null
            private set

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun build() = InstructorDto(this)
    }

    override fun equals(other: Any?): Boolean {
        if ((other == null) || (other::class.java != this::class.java)) return false
        val otherInstructorDto: InstructorDto = other as InstructorDto
        return this.id == otherInstructorDto.id && this.name == otherInstructorDto.name
    }
}