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

    override open fun toEntity(dto: CourseDto): Course = dto.let {
        Course.Builder().id(it.id!!)
            .name(it.name!!)
            .instructor(Instructor.Builder().id(it.instructorId!!).build())
            .build()
    }
}