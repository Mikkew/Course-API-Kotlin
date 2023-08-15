package com.mms.kotlin.spring.app.models.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.io.Serializable

data class CourseDto (

    @field:JsonProperty(value = "id")
    var id: Long?,

    @field:JsonProperty(value = "name")
    @field:NotBlank(message = "Name is required")
    @field:NotEmpty(message = "Name is required")
    var name: String?,

    @field:JsonProperty(value = "category")
    @field:NotBlank(message = "Category is required")
    @field:NotEmpty(message = "Category is required")
    var category: String?,

    @field:JsonProperty(value = "instructor", access = Access.READ_ONLY)
    var instructor: InstructorDto?,

    @field:JsonProperty(value = "instructorId", access = Access.WRITE_ONLY)
    @field:NotNull(message = "InstructorId is required")
    var instructorId: Long?
): Serializable {

    private constructor(builder: Builder) : this(
        builder.id,
        builder.name,
        builder.category,
        builder.instructor,
        builder.instructorId
    )

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var id: Long? = null
            private set
        var name: String? = null
            private set
        var category: String? = null
            private set
        var instructor: InstructorDto? = null
            private set
        var instructorId: Long? = null
            private set

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun category(category: String) = apply { this.category = category }
        fun instructor(instructor: InstructorDto) = apply { this.instructor = instructor }
        fun instructorId(instructorId: Long) = apply { this.instructorId = instructorId }
        fun build() = CourseDto(this)
    }

    override fun equals(other: Any?): Boolean {
        if ((other == null) || (other::class.java != this::class.java)) return false
        val otherCourseDto: CourseDto = other as CourseDto
        return this.id == otherCourseDto.id &&
               this.name == otherCourseDto.name &&
               this.category == otherCourseDto.category &&
               this.instructorId == otherCourseDto.instructorId &&
               this.instructor == otherCourseDto.instructor;

    }



    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (instructor?.hashCode() ?: 0)
        result = 31 * result + (instructorId?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "CourseDto(id=$id, name=$name, category=$category, instructor=$instructor)"
    }

}