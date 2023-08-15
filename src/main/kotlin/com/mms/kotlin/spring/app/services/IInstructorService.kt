package com.mms.kotlin.spring.app.services

import com.mms.kotlin.spring.app.models.dto.InstructorDto
import com.mms.kotlin.spring.app.models.entities.Instructor

interface IInstructorService {

    fun getInstructors(): List<InstructorDto>;

    fun getInstructorById(id: Long): InstructorDto;

    fun createInstructor(newInstructor: InstructorDto):InstructorDto;

    fun updateInstructor(instructor: InstructorDto): InstructorDto;

    fun deleteInstructor(id: Long): InstructorDto;
}