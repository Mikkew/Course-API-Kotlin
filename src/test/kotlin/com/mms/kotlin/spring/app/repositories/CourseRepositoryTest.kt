package com.mms.kotlin.spring.app.repositories

import com.mms.kotlin.spring.app.models.entities.Course
import com.mms.kotlin.spring.app.models.entities.Instructor
import com.mms.kotlin.spring.app.specifications.CourseSpecification
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*
import kotlin.NoSuchElementException


@DataJpaTest
@TestMethodOrder(value = OrderAnnotation::class)
class CourseRepositoryTest {

    companion object {
        private val LOGGER: KLogger = KotlinLogging.logger { }
    }

    @Autowired
    private lateinit var repository: ICourseRepository;

    @Autowired
    private lateinit var instructorRepository: IInstructorRepository;

    @Test
    @Order(value = 1)
    fun testFindAllCourse() {
        val courses: MutableList<Course> = repository.findAll();
        assertTrue(courses.isNotEmpty());
        assertEquals(1, courses.size)
        assertEquals(courses[0].category, "Math");
    }

    @Test
    @Order(value = 2)
    fun testSaveCourse() {
        val instructor: Instructor = instructorRepository.findById(1L).orElseThrow();
        val newCourse: Course = Course.Builder().name("Geometry").category("Math").instructor(instructor).build();

        val courseSave: Course = repository.save(newCourse);

        assertEquals(2L, courseSave.id);
        assertEquals(newCourse.name, courseSave.name);
        assertEquals(newCourse.category, courseSave.category);
        assertEquals(newCourse.instructor, courseSave.instructor);
    }

    @Test
    @Order(value = 3)
    fun testFindByIdCourse() {
        val course: Optional<Course> = repository.findById(1L);

        assertTrue(course.isPresent)
        assertEquals(1L, course.orElseThrow().id);
    }

    @ParameterizedTest
    @ValueSource(strings = ["Algebra"])
    @Order(value = 4)
    fun testFindAllSpecification(value: String) {
        val courses: MutableList<Course> = repository.findAll(CourseSpecification.nameContains(value));

        assertTrue(courses.isNotEmpty());
        assertEquals(value, courses[0].name);
    }

    @ParameterizedTest
    @ValueSource(strings = ["Math"])
    @Order(value = 5)
    fun testFindByCategory(value: String) {
        val courses: Optional<List<Course>> = repository.findByCategoryContains(value);

        assertFalse(courses.isEmpty);
        assertEquals(value, courses.get()[0].category);
    }

    @Test
    @Order(value = 6)
    fun testFindByIdCourseThrowException() {
        val course: Optional<Course> = repository.findById(3L);

        assertThrows(NoSuchElementException::class.java, course::orElseThrow);
        assertFalse(course.isPresent);
    }

    @ParameterizedTest
    @ValueSource(strings = ["Geometry"])
    @Order(value = 7)
    fun testFindAllSpecificationEmpty(value: String) {
        val courses: MutableList<Course> = repository.findAll(CourseSpecification.nameContains(value));

        assertEquals(0, courses.size)
        assertTrue(courses.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = ["Science"])
    @Order(value = 8)
    fun testFindByCategoryEmpty(value: String) {
        val courses: Optional<List<Course>> = repository.findByCategoryContains(value);

        assertEquals(0, courses.orElseThrow().size);
        assertTrue(courses.orElseThrow().isEmpty());
    }

    @Test
    @Order(value = 9)
    fun testUpdateCourse() {
        val instructor: Instructor = instructorRepository.findById(1L).orElseThrow();
        val newCourse: Course = Course.Builder().name("Geometry").category("Math").instructor(instructor).build();

        val courseSave: Course = repository.save(newCourse);

        assertEquals(3L, courseSave.id);
        assertEquals(newCourse.name, courseSave.name);
        assertEquals(newCourse.category, courseSave.category);
        assertEquals(newCourse.instructor, courseSave.instructor);

        newCourse.name = "Biology";
        newCourse.category = "Science";

        val courseUpdated: Course = repository.save(newCourse);

        assertEquals(3L, courseSave.id);
        assertEquals(newCourse.name, courseSave.name);
        assertEquals(newCourse.category, courseSave.category);
        assertEquals(newCourse.instructor, courseSave.instructor);
    }

    @Test
    @Order(value = 10)
    fun testDeleteCourse() {
        val course: Course = repository.findById(1L).orElseThrow();

        assertEquals(1L, course.id)
        assertEquals("Algebra", course.name);
        assertEquals("Math", course.category);

        repository.deleteById(course.id!!);

        assertThrows(NoSuchElementException::class.java) {
            repository.findById(1L).orElseThrow();
        }

        assertEquals(0, repository.findAll().size);
    }
}