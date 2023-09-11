package com.mms.kotlin.spring.app.mapper

import com.mms.kotlin.spring.app.data.DataUtils
import com.mms.kotlin.spring.app.models.dto.CourseDto
import com.mms.kotlin.spring.app.models.entities.Course
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CourseConverterTest {

    companion object {
        private val LOGGER: KLogger = KotlinLogging.logger { }
    }

    @Autowired
    lateinit var converter: CourseConverter;

    @Test
    fun testToDto() {
        val courseDto:CourseDto = converter.toDto(DataUtils.COURSE_ENTITY_001.get());

        assertEquals(DataUtils.COURSE_ENTITY_001.get().id, courseDto.id);
        assertEquals(DataUtils.COURSE_ENTITY_001.get().name, courseDto.name);
        assertEquals(DataUtils.COURSE_ENTITY_001.get().category, courseDto.category);
        assertEquals(DataUtils.COURSE_ENTITY_001.get().instructor!!.id, courseDto.instructor!!.id);
        assertEquals(DataUtils.COURSE_ENTITY_001.get().instructor!!.name, courseDto.instructor!!.name);
    }

    @Test
    fun testToEntity() {
        val courseEntity: Course = converter.toEntity(DataUtils.COURSE_DTO_001);

        assertEquals(DataUtils.COURSE_DTO_001.id, courseEntity.id);
        assertEquals(DataUtils.COURSE_DTO_001.name, courseEntity.name);
        assertEquals(DataUtils.COURSE_DTO_001.category, courseEntity.category);
        assertEquals(DataUtils.COURSE_DTO_001.instructor!!.id, courseEntity.instructor!!.id);
        assertEquals(DataUtils.COURSE_DTO_001.instructor!!.name, courseEntity.instructor!!.name);
    }
}