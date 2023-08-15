package com.mms.kotlin.spring.app.services

import com.mms.kotlin.spring.app.data.DataUtils
import com.mms.kotlin.spring.app.models.dto.InstructorDto
import com.mms.kotlin.spring.app.models.entities.Instructor
import com.mms.kotlin.spring.app.repositories.IInstructorRepository
import com.mms.kotlin.spring.app.utils.MockitoUtils
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime

@SpringBootTest
@TestMethodOrder(value = OrderAnnotation::class)
class InstructorServiceImplTest {

    companion object {
        private val LOGGER: KLogger = KotlinLogging.logger { }
    }

    @MockBean
    private lateinit var repository: IInstructorRepository;

    @Autowired
    private lateinit var service: IInstructorService;

    @Test
    @Order(value = 1)
    fun testGetInstructos() {

        `when`(repository.findAll()).thenReturn(DataUtils.LIST_INSTRUCTORS_ENTITIES);

        val listInstructors: List<InstructorDto> = service.getInstructors();

        assertEquals(DataUtils.LIST_INSTRUCTORS_ENTITIES[0], DataUtils.INSTRUCTOR_ENTITY_001.get());
        assertEquals(DataUtils.LIST_INSTRUCTORS_ENTITIES[1], DataUtils.INSTRUCTOR_ENTITY_002.get());
        assertEquals(DataUtils.LIST_INSTRUCTORS_ENTITIES[2], DataUtils.INSTRUCTOR_ENTITY_003.get());
        assertEquals(DataUtils.LIST_INSTRUCTORS_ENTITIES[0].id, listInstructors[0].id);
        assertEquals(DataUtils.LIST_INSTRUCTORS_ENTITIES[0].name, listInstructors[0].name);
        assertEquals(DataUtils.LIST_INSTRUCTORS_ENTITIES[1].id, listInstructors[1].id);
        assertEquals(DataUtils.LIST_INSTRUCTORS_ENTITIES[1].name, listInstructors[1].name);
        assertEquals(DataUtils.LIST_INSTRUCTORS_ENTITIES[2].id, listInstructors[2].id);
        assertEquals(DataUtils.LIST_INSTRUCTORS_ENTITIES[2].name, listInstructors[2].name);

        verify(repository, atLeastOnce()).findAll();
    }

    @Test
    fun testCreateInstructor() {
        val instructor: InstructorDto = InstructorDto.Builder().name("Beatriz Hernandez").build();

        `when`(repository.save(MockitoUtils.any<Instructor>())).thenAnswer(Answer<Any> {invocation: InvocationOnMock ->  run {
            val i: Instructor = invocation.getArgument(0) as Instructor;
            i.id=4L;
            i.createdAt = LocalDateTime.now();
            i.updatedAt = LocalDateTime.now();
            return@Answer i;
        }});

        val newInstructor: InstructorDto = service.createInstructor(instructor);

        assertEquals(4L, newInstructor.id);
        assertEquals(instructor.name, newInstructor.name);

        verify(repository, atLeastOnce()).save(MockitoUtils.any());
    }

    @Test
    @Order(value = 3)
    fun testGetInstructorById() {
        val instructorId: Long = 1L;

        `when`(repository.findById(anyLong())).thenReturn(DataUtils.INSTRUCTOR_ENTITY_001);

        val instructor: InstructorDto = service.getInstructorById(instructorId);

        assertEquals(DataUtils.INSTRUCTOR_ENTITY_001.get().id, instructor.id);
        assertEquals(DataUtils.INSTRUCTOR_ENTITY_001.get().name, instructor.name);

        verify(repository, atLeastOnce()).findById(anyLong());
    }

    @Test
    @Order(value = 4)
    fun testUpdateInstructor() {
        val updateInstructor: InstructorDto = DataUtils.INSTRUCTOR_DTO_003.copy(name = "Esteban Morales");

        `when`(repository.findById(anyLong())).thenReturn(DataUtils.INSTRUCTOR_ENTITY_003)

//        `when`(repository.findById(anyLong())).thenAnswer(Answer<Any> {invocation: InvocationOnMock -> run {
//            val i: Instructor = invocation.getArgument(0) as Instructor;
//            i.name= updateInstructor.name;
//            return@Answer i;
//        }});

        val instructor: InstructorDto = service.updateInstructor(updateInstructor);

        assertEquals(updateInstructor.id, instructor.id);
        assertEquals(updateInstructor.name, instructor.name);

        verify(repository, atLeastOnce()).findById(anyLong());
    }

    @Test
    @Order(value = 5)
    fun testDeleteInstructor() {
        val deleteInstructor: Long = 3L;

        `when`(repository.findById(anyLong())).thenReturn(DataUtils.INSTRUCTOR_ENTITY_003);

        val instructor: InstructorDto = service.deleteInstructor(deleteInstructor);

        assertEquals(deleteInstructor, instructor.id);

        verify(repository, atLeastOnce()).findById(anyLong());
        verify(repository, atLeastOnce()).deleteById(anyLong());
    }
}