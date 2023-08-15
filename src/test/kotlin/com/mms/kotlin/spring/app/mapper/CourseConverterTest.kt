package com.mms.kotlin.spring.app.mapper

import com.mms.kotlin.spring.app.data.DataUtils
import com.mms.kotlin.spring.app.models.dto.CourseDto
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

        LOGGER.info { "$courseDto" }

    }

    @Test
    fun testToEntity() {
    }
}