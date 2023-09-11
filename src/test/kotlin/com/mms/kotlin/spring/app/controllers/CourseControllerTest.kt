package com.mms.kotlin.spring.app.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mms.kotlin.spring.app.data.DataUtils
import com.mms.kotlin.spring.app.data.DataUtils.Companion.UPDATE_COURSE_REQUEST
import com.mms.kotlin.spring.app.utils.anyMockito
import com.mms.kotlin.spring.app.models.dto.CourseDto
import com.mms.kotlin.spring.app.services.ICourseService
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc

import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.stream.Stream

@WebMvcTest(CourseController::class)
@TestMethodOrder(value = OrderAnnotation::class)
class CourseControllerTest {

    @MockBean
    private lateinit var service: ICourseService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper();
    }

    @Test
    @Order(value = 1)
    fun testListCourses() {
//        val coursesList: List<CourseDto> = listOf(DataUtils.COURSE_DTO_001, DataUtils.COURSE_DTO_002, DataUtils.COURSE_DTO_003);
        val coursesList: List<CourseDto> = DataUtils.LIST_COURSES_DTO;

        `when`(service.getCourses()).thenReturn(coursesList);

        mockMvc.perform(get("/course"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(DataUtils.COURSE_DTO_001.id))
            .andExpect(jsonPath("$[0].name").value(DataUtils.COURSE_DTO_001.name))
            .andExpect(jsonPath("$[0].category").value(DataUtils.COURSE_DTO_001.category))
            .andExpect(jsonPath("$[1].id").value(DataUtils.COURSE_DTO_002.id))
            .andExpect(jsonPath("$[1].name").value(DataUtils.COURSE_DTO_002.name))
            .andExpect(jsonPath("$[1].category").value(DataUtils.COURSE_DTO_002.category))
            .andExpect(jsonPath("$[2].id").value(DataUtils.COURSE_DTO_003.id))
            .andExpect(jsonPath("$[2].name").value(DataUtils.COURSE_DTO_003.name))
            .andExpect(jsonPath("$[2].category").value(DataUtils.COURSE_DTO_003.category))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").value(hasSize<Any>(3)));

        verify(service, times(1)).getCourses();
    }

    @Test
    @Order(value = 2)
    fun testSaveCourse() {
//        val newCourse: CourseDto = CourseDto.Builder().name("Aritmetic").category("Math").instructorId(1L).build();

        `when`(service.createCourse(anyMockito())).then(Answer<Any> { invocation: InvocationOnMock ->
            val c: CourseDto = invocation.getArgument(0)
            c.id = 4L
            return@Answer c
        })

        mockMvc.perform(post("/course/").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(DataUtils.CREATE_COURSE_REQUEST)))
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value<Int>(`is`(4)))
//            .andExpect(jsonPath("$.name").value<String>(`is`(newCourse.name)))
//            .andExpect(jsonPath("$.category").value(newCourse.category))

    }

    @Test
    @Order(value = 3)
    fun testUpdateCourse() {
//        val course: CourseDto = DataUtils.COURSE_002.copy();
//        course.name = "Calculus";

        `when`(service.updateCourse(anyMockito())).then(Answer<Any> { invocation: InvocationOnMock ->
            val c: CourseDto = invocation.getArgument(0) as CourseDto
            c.name = UPDATE_COURSE_REQUEST.get("name") as String;
            c.category = c.category
            return@Answer c
        })

        mockMvc.perform(put("/course/").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(UPDATE_COURSE_REQUEST)))
            .andExpect(status().isAccepted)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value<Int>(`is`(course.id!!.toInt())))
//            .andExpect(jsonPath("$.name").value<String>(`is`(course.name)))
//            .andExpect(jsonPath("$.category").value(course.category))
            .andExpect(jsonPath("$.id").value<Int>(`is`(UPDATE_COURSE_REQUEST["id"])))
            .andExpect(jsonPath("$.name").value<String>(`is`(UPDATE_COURSE_REQUEST["name"])))
            .andExpect(jsonPath("$.category").value(UPDATE_COURSE_REQUEST["category"]))
    }

    @Test
    @Order(value = 4)
    fun testDeleteCourse() {
        `when`(service.deleteCourse(anyLong())).thenReturn(DataUtils.COURSE_DTO_003)

        mockMvc.perform(delete("/course/3"))
            .andExpect(status().isAccepted)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value<Int>(`is`(DataUtils.COURSE_DTO_003.id!!.toInt())))
            .andExpect(jsonPath("$.name").value(`is`(DataUtils.COURSE_DTO_003.name)))
            .andExpect(jsonPath("$.category").value(DataUtils.COURSE_DTO_003.category))
    }

    @ParameterizedTest
    @Order(value = 5)
    @MethodSource("provideParameters")
    fun testGetCoursesByName(key: String, value: String) {
        val listCoursesFiltered = DataUtils.LIST_COURSES_DTO.filter { it.name!!.contains(value)};
        `when`(service.getCoursesFilters(anyMockito())).thenReturn(listCoursesFiltered);

        mockMvc.perform(get("/course/filter")
                .param(key, value))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name").value(value))
    }

    @ParameterizedTest
    @Order(value = 6)
    @ValueSource(strings = ["Math"])
    fun testGetCoursesByCategory(value: String) {
        val listCoursesFiltered = DataUtils.LIST_COURSES_DTO.filter { it.category!!.contains(value) };
        `when`(service.getCoursesByCategory(anyMockito())).thenReturn(listCoursesFiltered);

        mockMvc.perform(get("/course/category/${value}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].category").value(value))
    }

    companion object {
        @JvmStatic
        public fun provideParameters() = Stream.of(
            Arguments.of("name", "Algebra")
        )
    }
}