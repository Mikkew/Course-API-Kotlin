package com.mms.kotlin.spring.app.services

import org.springframework.data.jpa.domain.Specification;

import com.mms.kotlin.spring.app.models.dto.CourseDto
import com.mms.kotlin.spring.app.models.entities.Course

interface ICourseService {

    fun getCourses(): List<CourseDto>;

    fun getCoursesFilters(spec: Specification<Course>): List<CourseDto>;

    fun getCoursesByCategory(category: String): List<CourseDto>;

    fun getCourse(id: Long): CourseDto;

    fun createCourse(course: CourseDto): CourseDto;

    fun updateCourse(course: CourseDto): CourseDto;

    fun deleteCourse(id: Long): CourseDto;
}