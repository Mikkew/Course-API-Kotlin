package com.mms.kotlin.spring.app.mapper

import com.mms.kotlin.spring.app.models.dto.CourseDto
import com.mms.kotlin.spring.app.models.dto.InstructorDto
import com.mms.kotlin.spring.app.models.entities.Course
import com.mms.kotlin.spring.app.models.entities.Instructor
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class CourseConverter : AbstractConverter<Course, CourseDto>() {

    override open fun toDto(entity: Course): CourseDto = entity.let {
        CourseDto.Builder()
            .id(it.id!!)
            .name(it.name!!)
            .category(it.category!!)
            .instructor(
                Optional.of(
                    InstructorDto.Builder().id(it.instructor!!.id!!).name(it.instructor!!.name!!).build()
                ).orElse(null)
            )
            .build()
    }

    override fun toEntity(dto: CourseDto): Course = dto.let {
        val instructor: Instructor? = it.instructor?.let { instructorDto ->
            Instructor(id = instructorDto.id, name = instructorDto.name, courses = null)
        } ?: it.instructorId?.let { instructorId -> Instructor.Builder().id(instructorId).build() }

        Course(id = it.id, name = it.name, category = it.category, instructor = instructor)
    }
}