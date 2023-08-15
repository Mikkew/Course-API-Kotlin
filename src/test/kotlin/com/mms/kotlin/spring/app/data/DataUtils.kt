package com.mms.kotlin.spring.app.data

import com.mms.kotlin.spring.app.models.dto.CourseDto
import com.mms.kotlin.spring.app.models.dto.InstructorDto
import com.mms.kotlin.spring.app.models.entities.Course
import com.mms.kotlin.spring.app.models.entities.Instructor
import java.util.Optional

class DataUtils {

    companion object {
        @JvmStatic
        val INSTRUCTOR_DTO_001: InstructorDto = InstructorDto.Builder().id(1L).name("Ancelmo Gutierrez").build();
        val INSTRUCTOR_DTO_002: InstructorDto = InstructorDto.Builder().id(2L).name("Herminia Grajales").build();
        val INSTRUCTOR_DTO_003: InstructorDto = InstructorDto.Builder().id(3L).name("Joaquin Sanchez").build();

        val LIST_INSTRUCTORS_DTO: List<InstructorDto> = listOf(INSTRUCTOR_DTO_001, INSTRUCTOR_DTO_002, INSTRUCTOR_DTO_003);

        val INSTRUCTOR_ENTITY_001: Optional<Instructor> = Optional.of(Instructor.Builder().id(1L).name("Ancelmo Gutierrez").build());
        val INSTRUCTOR_ENTITY_002: Optional<Instructor> = Optional.of(Instructor.Builder().id(2L).name("Herminia Grajales").build());
        val INSTRUCTOR_ENTITY_003: Optional<Instructor> = Optional.of(Instructor.Builder().id(3L).name("Joaquin Sanchez").build());

        val LIST_INSTRUCTORS_ENTITIES: List<Instructor> = listOf(INSTRUCTOR_ENTITY_001.get(), INSTRUCTOR_ENTITY_002.get(), INSTRUCTOR_ENTITY_003.get())

        val COURSE_DTO_001: CourseDto = CourseDto.Builder().id(1L).name("Algebra").category("Math")
            .instructor(INSTRUCTOR_DTO_001).build();
        val COURSE_DTO_002: CourseDto = CourseDto.Builder().id(2L).name("Geometry").category("Math")
            .instructor(INSTRUCTOR_DTO_002).build();
        val COURSE_DTO_003: CourseDto = CourseDto.Builder().id(3L).name("Calculus").category("Math")
            .instructor(INSTRUCTOR_DTO_003).build();

        val LIST_COURSES_DTO: List<CourseDto> = listOf(COURSE_DTO_001, COURSE_DTO_002, COURSE_DTO_003);

        val COURSE_ENTITY_001: Optional<Course> = Optional.of(Course.Builder().id(1L).name("Algebra").category("Math")
            .instructor(INSTRUCTOR_ENTITY_001.get()).build());
        val COURSE_ENTITY_002: Optional<Course> = Optional.of(Course.Builder().id(2L).name("Geometry").category("Math")
            .instructor(INSTRUCTOR_ENTITY_002.get()).build());
        val COURSE_ENTITY_003: Optional<Course> = Optional.of(Course.Builder().id(3L).name("Calculus").category("Math")
            .instructor(INSTRUCTOR_ENTITY_003.get()).build());

        val LIST_COURSES_ENTITIES: List<Course> = listOf(COURSE_ENTITY_001.get(), COURSE_ENTITY_002.get(), COURSE_ENTITY_003.get());

        val CREATE_COURSE_REQUEST: Map<String, Any> = mutableMapOf("name" to "Geometry", "category" to "Math", "instructorId" to 2);
        val UPDATE_COURSE_REQUEST: Map<String, Any> = mutableMapOf("id" to 2, "name" to "Geometry", "category" to "Math", "instructorId" to 1);

        val CREATE_INSTRUCTOR_REQUEST: Map<String, Any> = mutableMapOf("name" to "Rodolfo Velez")
        val UPDATE_INSTRUCTOR_REQUEST: Map<String, Any> = mutableMapOf("id" to 2, "name" to "Rodolfo Ortega")

    }
}