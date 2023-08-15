package com.mms.kotlin.spring.app.services.impl

import com.mms.kotlin.spring.app.exceptions.ResourceNotFoundException
import com.mms.kotlin.spring.app.models.dto.InstructorDto
import com.mms.kotlin.spring.app.models.entities.Instructor
import com.mms.kotlin.spring.app.repositories.IInstructorRepository
import com.mms.kotlin.spring.app.services.IInstructorService
import mu.KLogger
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InstructorServiceImpl @Autowired constructor(
    private val repository: IInstructorRepository
) : IInstructorService {

    companion object {
        private val LOGGER: KLogger = KotlinLogging.logger { }
        private val MESSAGE_RESOURCE_NOT_FOUND: String = "Instructor Not Found";
    }

//    @Autowired
//    constructor(repository: IInstructorRepository) {
//        this.repository = repository
//    }

    @Transactional(readOnly = true)
    override fun getInstructors(): List<InstructorDto> {
        val instructors: List<Instructor> = repository.findAll();
        if(instructors.isEmpty()) throw ResourceNotFoundException("Instructors Not Found");

        LOGGER.info { "List instructors $instructors" }
        return instructors.map { InstructorDto.Builder().id(it.id!!).name(it.name!!).build() };
    }

    @Transactional(readOnly = true)
    override fun getInstructorById(id: Long): InstructorDto {
        val instructor: Instructor = repository.findById(id).orElseThrow{ throw ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND) }

        LOGGER.info { "Get instructor $instructor" }
        return instructor.let { InstructorDto.Builder().id(it.id!!).name(it.name!!).build() };
    }

    @Transactional
    override fun createInstructor(newInstructor: InstructorDto): InstructorDto {
        val instructorEntity: Instructor = newInstructor.let { Instructor.Builder().name(it.name!!).build() };
        repository.save(instructorEntity);
        return instructorEntity.let { InstructorDto.Builder().id(it.id!!).name(it.name!!).build() };
    }

    @Transactional
    override fun updateInstructor(instructor: InstructorDto): InstructorDto {
        val instructorEntity: Instructor = repository.findById(instructor.id!!).orElseThrow({ throw ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND) });
        instructorEntity.name = instructor.name

        LOGGER.info { "Update instructor : $instructorEntity" }
        return instructorEntity.let { InstructorDto.Builder().id(it.id!!).name(it.name!!).build() };
    }

    @Transactional
    override fun deleteInstructor(id: Long): InstructorDto {
        val instructorEntity: Instructor = repository.findById(id).orElseThrow({ throw ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND) });
        repository.deleteById(id);

        LOGGER.info { "Delete instructor : $instructorEntity" }
        return instructorEntity.let { InstructorDto.Builder().id(it.id!!).name(it.name!!).build() };
    }
}