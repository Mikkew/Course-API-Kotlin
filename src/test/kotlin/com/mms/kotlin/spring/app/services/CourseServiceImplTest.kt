package com.mms.kotlin.spring.app.services

import com.mms.kotlin.spring.app.data.DataUtils
import com.mms.kotlin.spring.app.exceptions.ResourceNotFoundException
import com.mms.kotlin.spring.app.mapper.CourseConverter
import com.mms.kotlin.spring.app.models.dto.CourseDto
import com.mms.kotlin.spring.app.models.entities.Course
import com.mms.kotlin.spring.app.repositories.ICourseRepository
import com.mms.kotlin.spring.app.repositories.IInstructorRepository
import com.mms.kotlin.spring.app.services.impl.CourseServiceImpl
import com.mms.kotlin.spring.app.specifications.CourseSpecification
import com.mms.kotlin.spring.app.utils.MockitoUtils
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime
import java.util.Optional

@SpringBootTest
@TestMethodOrder(value = OrderAnnotation::class)
class CourseServiceImplTest {

    companion object {
        private val LOGGER: KLogger = KotlinLogging.logger { }
    }

    @MockBean
    private lateinit var repository: ICourseRepository;

//    @MockBean
//    private lateinit var converter: CourseConverter;

    @MockBean
    private lateinit var instructorRepository: IInstructorRepository;

    @Autowired
    private lateinit var service: CourseServiceImpl;

    @Test
    @Order(value = 1)
    fun testGetCourses() {
//        val coursesEntityList: List<Course> = DataUtils.LIST_COURSES_DTO.map { Course.Builder().id(it.id!!).name(it.name!!).category(it.category!!).build() }

        `when`(repository.findAll()).thenReturn(DataUtils.LIST_COURSES_ENTITIES);

        val coursesList: List<CourseDto> = service.getCourses();

        assertEquals(DataUtils.LIST_COURSES_ENTITIES[0], DataUtils.COURSE_ENTITY_001.get());
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[1], DataUtils.COURSE_ENTITY_002.get());
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[2], DataUtils.COURSE_ENTITY_003.get());
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[0].id, coursesList[0].id);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[0].name, coursesList[0].name);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[0].category, coursesList[0].category);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[0].instructor!!.id, coursesList[0].instructor!!.id);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[0].instructor!!.name, coursesList[0].instructor!!.name);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[1].id, coursesList[1].id);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[1].name, coursesList[1].name);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[1].category, coursesList[1].category);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[1].instructor!!.id, coursesList[1].instructor!!.id);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[1].instructor!!.name, coursesList[1].instructor!!.name);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[2].id, coursesList[2].id);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[2].name, coursesList[2].name);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[2].category, coursesList[2].category);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[2].instructor!!.id, coursesList[2].instructor!!.id);
        assertEquals(DataUtils.LIST_COURSES_ENTITIES[2].instructor!!.name, coursesList[2].instructor!!.name);

        verify(repository, atLeastOnce()).findAll()
    }

    @Test
    @Order(value = 2)
    fun testCreateCourse() {
        val newCourse: CourseDto = CourseDto.Builder().name("Stadistic").category("Math").instructorId(1L).build();

        `when`(instructorRepository.findById(anyLong())).thenReturn(DataUtils.INSTRUCTOR_ENTITY_001);

        `when`(repository.save(MockitoUtils.any())).thenAnswer(Answer<Any> { invocation: InvocationOnMock -> run {
            var c: Course = invocation.getArgument(0) as Course;
            c.id = 4L
            c.createdAt = LocalDateTime.now();
            c.updatedAt = LocalDateTime.now();
            return@Answer c;
        }});

        val course: CourseDto = service.createCourse(newCourse);

        assertEquals(4L, course.id);
        assertEquals(newCourse.name, course.name);
        assertEquals(newCourse.category, course.category);
        assertEquals(newCourse.instructorId, course.instructor!!.id);

        verify(repository, atLeastOnce()).save(MockitoUtils.any());
        verify(instructorRepository).findById(MockitoUtils.any());
    }

    @Test
    @Order(value = 3)
    fun testCourseById() {
        `when`(repository.findById(anyLong())).thenReturn(DataUtils.COURSE_ENTITY_001);

        val course: CourseDto = service.getCourse(1L);

        assertEquals(DataUtils.COURSE_ENTITY_001.get().id, course.id);
        assertEquals(DataUtils.COURSE_ENTITY_001.get().name, course.name);
        assertEquals(DataUtils.COURSE_ENTITY_001.get().category, course.category);
        assertEquals(DataUtils.COURSE_ENTITY_001.get().instructor!!.id, course.instructor!!.id);
        assertEquals(DataUtils.COURSE_ENTITY_001.get().instructor!!.name, course.instructor!!.name);

        verify(repository, atLeastOnce()).findById(anyLong());
        verify(instructorRepository, times(0)).findById(MockitoUtils.any());
    }

    @Test
    @Order(value = 4)
    fun testUpdateCourse() {
        val updateCourse: CourseDto = DataUtils.COURSE_DTO_003.copy(name = "Aritmetica", instructorId = 2L);

        `when`(repository.findById(anyLong())).thenReturn(DataUtils.COURSE_ENTITY_003);

        `when`(instructorRepository.findById(anyLong())).thenReturn(DataUtils.INSTRUCTOR_ENTITY_003);

        `when`(instructorRepository.findById(anyLong())).thenReturn(DataUtils.INSTRUCTOR_ENTITY_002);

//        `when`(repository.save(MockitoUtils.any())).thenAnswer(Answer<Any> { invocation: InvocationOnMock -> run {
//            val c: Course = invocation.getArgument(0) as Course;
//            c.name = updateCourse.name;
//            c.instructor = DataUtils.INSTRUCTOR_ENTITY_002.get();
//            return@Answer c;
//        }});

        val course = service.updateCourse(updateCourse);

        assertEquals(updateCourse.id, course.id);
        assertEquals(updateCourse.name, course.name);
        assertEquals(updateCourse.category, course.category);
        assertEquals(updateCourse.instructorId, DataUtils.INSTRUCTOR_ENTITY_002.get().id);

        verify(repository, atLeastOnce()).findById(anyLong());
        verify(repository, times(0)).save(MockitoUtils.any());
        verify(instructorRepository, atLeastOnce()).findById(anyLong());
    }

    @Test
    @Order(value = 5)
    fun testDeleteCourse() {
        val deleteCourse: Long = 3L;

        `when`(repository.findById(anyLong())).thenReturn(DataUtils.COURSE_ENTITY_003);
//        `when`(repository.deleteById(anyLong())).thenReturn(null);

        val course: CourseDto = service.deleteCourse(deleteCourse);

        assertEquals(deleteCourse, course.id);

        verify(repository, times(1)).deleteById(anyLong());
        verify(repository, atLeastOnce()).findById(anyLong());
    }

    @ParameterizedTest
    @ValueSource(strings = ["Algebra"])
    @Order(value = 6)
    fun testGetCourseByName(value: String) {
        val listCourseFiltered: List<Course> = DataUtils.LIST_COURSES_ENTITIES.filter { it.name!!.contains(value) };

        `when`(repository.findAll(MockitoUtils.any<Specification<Course>>())).thenReturn(listCourseFiltered);

        val listCourses: List<CourseDto> = service.getCoursesFilters(CourseSpecification.nameContains(anyString()));
        LOGGER.info { "$listCourses" }

        assertEquals(listCourseFiltered[0].id, listCourses[0].id);
        assertEquals(listCourseFiltered[0].name, listCourses[0].name);
        assertEquals(listCourseFiltered[0].category, listCourses[0].category);
        assertEquals(listCourseFiltered[0].instructor!!.id, listCourses[0].instructor!!.id);
        assertEquals(listCourseFiltered[0].instructor!!.name, listCourses[0].instructor!!.name);

        verify(repository, atLeastOnce()).findAll(MockitoUtils.any<Specification<Course>>());
    }

    @ParameterizedTest
    @ValueSource(strings = ["Math"])
    @Order(value = 7)
    fun testGetCourseByCategory(value: String) {
        val listCourseFiltered: Optional<List<Course>> = Optional.of(DataUtils.LIST_COURSES_ENTITIES.filter { it.category!!.contains(value) });

        `when`(repository.findByCategoryContains(anyString())).thenReturn(listCourseFiltered);

        val listCourses: List<CourseDto> = service.getCoursesByCategory(anyString());

        assertEquals(listCourseFiltered.get()[0].id, listCourses[0].id);
        assertEquals(listCourseFiltered.get()[0].name, listCourses[0].name);
        assertEquals(listCourseFiltered.get()[0].category, listCourses[0].category);
        assertEquals(listCourseFiltered.get()[0].instructor!!.id, listCourses[0].instructor!!.id);
        assertEquals(listCourseFiltered.get()[0].instructor!!.name, listCourses[0].instructor!!.name);

        verify(repository, atLeastOnce()).findByCategoryContains(anyString());
    }

    @Test
    @Order(value = 8)
    fun testCourseByIdThrowException() {
        `when`(repository.findById(anyLong())).thenThrow(ResourceNotFoundException::class.java)

        assertThrows(ResourceNotFoundException::class.java) {
            val course: CourseDto = service.getCourse(7L);
        }

        verify(repository, atLeastOnce()).findById(anyLong());
        verify(instructorRepository, times(0)).findById(MockitoUtils.any());
    }

    @ParameterizedTest
    @ValueSource(strings = ["Biology"])
    @Order(value = 9)
    fun testGetCourseByNameThrowException(value: String) {
        `when`(repository.findAll(MockitoUtils.any<Specification<Course>>())).thenThrow(ResourceNotFoundException::class.java);

        assertThrows(ResourceNotFoundException::class.java) {
            val listCourses: List<CourseDto> = service.getCoursesFilters(CourseSpecification.nameContains(value));
        }

        verify(repository, atLeastOnce()).findAll(MockitoUtils.any<Specification<Course>>());
    }

    @ParameterizedTest
    @ValueSource(strings = ["Science"])
    @Order(value = 10)
    fun testGetCourseByCategoryThrowException(value: String) {
        `when`(repository.findByCategoryContains(anyString())).thenThrow(ResourceNotFoundException::class.java);

        assertThrows(ResourceNotFoundException::class.java) {
            val listCourses: List<CourseDto> = service.getCoursesByCategory(anyString());
        }

        verify(repository, atLeastOnce()).findByCategoryContains(anyString());
    }
}