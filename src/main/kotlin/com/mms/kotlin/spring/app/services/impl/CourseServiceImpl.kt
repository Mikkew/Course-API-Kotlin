package com.mms.kotlin.spring.app.services.impl

import com.mms.kotlin.spring.app.exceptions.ResourceNotFoundException
import com.mms.kotlin.spring.app.mapper.CourseConverter
import com.mms.kotlin.spring.app.models.dto.CourseDto
import com.mms.kotlin.spring.app.models.entities.Course
import com.mms.kotlin.spring.app.models.entities.Instructor
import com.mms.kotlin.spring.app.repositories.ICourseRepository
import com.mms.kotlin.spring.app.repositories.IInstructorRepository
import com.mms.kotlin.spring.app.services.ICourseService
import mu.KLogger
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CourseServiceImpl @Autowired constructor (
    private val courseRepository: ICourseRepository,
    private val converter: CourseConverter,
    private val instructorRepository: IInstructorRepository
): ICourseService {

    companion object {
        private val LOGGER: KLogger = KotlinLogging.logger { }
        private val MESSAGE_RESOURCE_NOT_FOUND_COURSE: String = "Course Not Found";
        private val MESSAGE_RESOURCE_EMPTY_LIST_COURSES: String = "Courses Not Found";
        private val MESSAGE_RESOURCE_NOT_FOUND_INSTRUCTOR: String = "Instructor Not Found";
    }

//    @Autowired
//    private lateinit var courseRepository: ICourseRepository;
//
//    @Autowired
//    private lateinit var converter: CourseConverter;
//
//    @Autowired
//    private lateinit var instructorRepository: IInstructorRepository

//    private lateinit var courseRepository: ICourseRepository;
//    private lateinit var converter: CourseConverter;
//    private lateinit var instructorRepository: IInstructorRepository

//    @Autowired
//    constructor (
//        courseRepository: ICourseRepository,
//        converter: CourseConverter,
//        instructorRepository: IInstructorRepository
//    ) {
//        this.courseRepository = courseRepository
//        this.converter = converter
//        this.instructorRepository = instructorRepository
//    }

    @Transactional(readOnly = true)
    override fun getCourses(): List<CourseDto> {
        val courses: List<Course> = courseRepository.findAll().ifEmpty { throw ResourceNotFoundException(MESSAGE_RESOURCE_EMPTY_LIST_COURSES) };

        LOGGER.info { "List courses : $courses" }
        return converter.toDto(courses);
    }

    @Transactional(readOnly = true)
    override fun getCoursesFilters(spec: Specification<Course>): List<CourseDto> {
        val courses: List<Course> = courseRepository.findAll(spec).ifEmpty { throw ResourceNotFoundException(MESSAGE_RESOURCE_EMPTY_LIST_COURSES) };
        LOGGER.info { "List courses filtered : $courses" }
        return converter.toDto(courses);
    }

    @Transactional(readOnly = true)
    override fun getCoursesByCategory(category: String): List<CourseDto> {
        val courses: List<Course> = courseRepository.findByCategoryContains(category).orElseThrow{ throw ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND_COURSE) }
        LOGGER.info { "List courses get By Category: $courses" }
        return converter.toDto(courses);
    }

    @Transactional(readOnly = true)
    override fun getCourse(id: Long): CourseDto {
        val course: Course = courseRepository.findById(id).orElseThrow{ throw ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND_COURSE) }
        LOGGER.info { "Get course : $course" }
        return converter.toDto(course);
    }

    @Transactional
    override fun createCourse(course: CourseDto): CourseDto {
        var courseEntity: Course = course.let { Course.Builder().name(it.name!!).category(it.category!!).build() }
        val instructorEntity: Instructor = instructorRepository.findById(course.instructorId!!).orElseThrow{ throw ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND_INSTRUCTOR) }
        courseEntity.instructor = instructorEntity
        courseEntity = courseRepository.save(courseEntity);
        LOGGER.info("Save course is : $courseEntity")

        return  converter.toDto(courseEntity);
    }

    @Transactional
    override fun updateCourse(course: CourseDto): CourseDto {
        val courseEntity: Course = courseRepository.findById(course.id!!).orElseThrow{ throw ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND_COURSE) }
        courseEntity.category = course.category
        courseEntity.name = course.name
        courseEntity.instructor = instructorRepository.findById(course.instructorId!!).orElse(courseEntity.instructor);
        LOGGER.info { "Update course : $courseEntity" }
        return converter.toDto(courseEntity);

    }

    @Transactional
    override fun deleteCourse(id: Long): CourseDto {
        val courseEntity: Course = courseRepository.findById(id).orElseThrow{ throw ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND_COURSE) }
        courseRepository.deleteById(id)
        LOGGER.info { "Delete course : $courseEntity" }
        return converter.toDto(courseEntity);
    }
}