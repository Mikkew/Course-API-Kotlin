package com.mms.kotlin.spring.app.specifications

import com.mms.kotlin.spring.app.models.entities.Course
import org.springframework.data.jpa.domain.Specification

class CourseSpecification {

    companion object {

        fun nameContains(name: String): Specification<Course> {
            return Specification<Course> { course, query, builder -> builder.like(course.get("name"), name) }
        }
    }
}