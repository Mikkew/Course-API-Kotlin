package com.mms.kotlin.spring.app.repositories

import com.mms.kotlin.spring.app.models.entities.Instructor
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.junit.jupiter.api.Assertions.*
import java.util.Optional

@DataJpaTest
@TestMethodOrder(value = OrderAnnotation::class)
class InstructorRepositoryTest {

    companion object {
        private val LOGGER: KLogger = KotlinLogging.logger { }
    }

    @Autowired
    private lateinit var repository: IInstructorRepository;

    @Test
    @Order(value = 1)
    fun testFindAllInstructors() {
        val instructors: MutableList<Instructor> = repository.findAll();

        assertTrue(instructors.isNotEmpty())
        assertEquals(1, instructors.size)
    }

    @Test
    @Order(value = 2)
    fun testSaveInstructor() {
        val newInstructor: Instructor = Instructor.Builder().name("Pedro Sanchez").build();

        val instructorSaved: Instructor = repository.save(newInstructor);

        assertEquals(2L, instructorSaved.id);
        assertEquals(newInstructor.name, instructorSaved.name);
    }

    @Test
    @Order(value = 3)
    fun testFindByIdInstructor() {
        val instructor: Optional<Instructor> = repository.findById(1L);

        assertTrue(instructor.isPresent);
        assertEquals(1L, instructor.get().id)
    }
    @Test
    @Order(value = 4)
    fun testFindByIdInstructorThrowException() {
        val instructor: Optional<Instructor> = repository.findById(2L);

        assertThrows(NoSuchElementException::class.java, instructor::orElseThrow);
        assertTrue(instructor.isEmpty);

    }

    @Test
    @Order(value = 5)
    fun testUpdateInstructor() {
        val newInstructor: Instructor = Instructor.Builder().name("Pedro Sanchez").build();

        val instructorSaved: Instructor = repository.save(newInstructor);

        assertEquals(3L, instructorSaved.id);
        assertEquals(newInstructor.name, instructorSaved.name);

        newInstructor.name = "Juan Gutierrez";

        val instructorUpdated: Instructor = repository.save(newInstructor);

        assertEquals(3L, instructorSaved.id);
        assertEquals(newInstructor.name, instructorSaved.name);
    }

    @Test
    @Order(value = 6)
    fun testDeleteInstructor() {
        val instructor: Instructor = repository.findById(1L).orElseThrow();

        assertEquals(1L, instructor.id);
        assertEquals("Ancelmo Gutierrez", instructor.name);

        repository.deleteById(instructor.id!!);

        assertThrows(NoSuchElementException::class.java) {
            repository.findById(1L).orElseThrow();
        }

        assertEquals(0, repository.findAll().size);
    }
}