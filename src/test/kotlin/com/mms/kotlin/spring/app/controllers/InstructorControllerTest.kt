package com.mms.kotlin.spring.app.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mms.kotlin.spring.app.data.DataUtils
import com.mms.kotlin.spring.app.data.DataUtils.Companion.CREATE_INSTRUCTOR_REQUEST
import com.mms.kotlin.spring.app.data.DataUtils.Companion.UPDATE_INSTRUCTOR_REQUEST
import com.mms.kotlin.spring.app.models.dto.InstructorDto
import com.mms.kotlin.spring.app.services.IInstructorService
import com.mms.kotlin.spring.app.utils.MockitoUtils
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(InstructorController::class)
@TestMethodOrder(value = OrderAnnotation::class)
class InstructorControllerTest {

    @MockBean
    private lateinit var service: IInstructorService;

    @Autowired
    private lateinit var mockMvc: MockMvc;

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper();
    }

    @Test
    @Order(value = 1)
    fun testListInstructors() {
        val instructors: List<InstructorDto> = DataUtils.LIST_INSTRUCTORS_DTO;

        `when`(service.getInstructors()).thenReturn(instructors);

        mockMvc.perform(get("/instructor"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(DataUtils.INSTRUCTOR_DTO_001.id))
            .andExpect(jsonPath("$[0].name").value(DataUtils.INSTRUCTOR_DTO_001.name))
            .andExpect(jsonPath("$[1].id").value(DataUtils.INSTRUCTOR_DTO_002.id))
            .andExpect(jsonPath("$[1].name").value(DataUtils.INSTRUCTOR_DTO_002.name))
            .andExpect(jsonPath("$[2].id").value(DataUtils.INSTRUCTOR_DTO_003.id))
            .andExpect(jsonPath("$[2].name").value(DataUtils.INSTRUCTOR_DTO_003.name))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").value(Matchers.hasSize<Any>(3)));

        verify(service, atLeastOnce()).getInstructors();
    }

    @Test
    @Order(value = 2)
    fun testSaveInstructor() {

        `when`(service.createInstructor(MockitoUtils.any())).thenAnswer(Answer<Any> { invocation: InvocationOnMock -> run {
            val i: InstructorDto = invocation.getArgument(0);
            i.id = 4L
            return@Answer i
        }});

        mockMvc.perform(post("/instructor").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(DataUtils.CREATE_INSTRUCTOR_REQUEST)))
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(4))
            .andExpect(jsonPath("$.name").value(CREATE_INSTRUCTOR_REQUEST["name"]));

        verify(service, atLeastOnce()).createInstructor(MockitoUtils.any());
    }

    @Test
    @Order(value = 3)
    fun testCourseById() {

        `when`(service.getInstructorById(anyLong())).thenReturn(DataUtils.INSTRUCTOR_DTO_002);

        mockMvc.perform(get("/instructor/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(DataUtils.INSTRUCTOR_DTO_002.id))
            .andExpect(jsonPath("$.name").value(DataUtils.INSTRUCTOR_DTO_002.name));

        verify(service, atLeastOnce()).getInstructorById(anyLong());
    }

    @Test
    @Order(value = 4)
    fun testUpdateCourse() {

        `when`(service.updateInstructor(MockitoUtils.any())).thenAnswer(Answer<Any> { invocation: InvocationOnMock -> run {
            val i: InstructorDto = invocation.getArgument(0) as InstructorDto;
            i.name = UPDATE_INSTRUCTOR_REQUEST["name"] as String;
            return@Answer i;
        }});

        mockMvc.perform(put("/instructor").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(UPDATE_INSTRUCTOR_REQUEST)))
            .andExpect(status().isAccepted)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(UPDATE_INSTRUCTOR_REQUEST["id"]))
            .andExpect(jsonPath("$.name").value(UPDATE_INSTRUCTOR_REQUEST["name"]));

        verify(service, atLeastOnce()).updateInstructor(MockitoUtils.any());
    }

    @Test
    @Order(value = 5)
    fun testDeleteCourse() {

        `when`(service.deleteInstructor(anyLong())).thenReturn(DataUtils.INSTRUCTOR_DTO_003);

        mockMvc.perform(delete("/instructor/3"))
            .andExpect(status().isAccepted)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(DataUtils.INSTRUCTOR_DTO_003.id))
            .andExpect(jsonPath("$.name").value(DataUtils.INSTRUCTOR_DTO_003.name));

        verify(service, atLeastOnce()).deleteInstructor(anyLong());
    }
}