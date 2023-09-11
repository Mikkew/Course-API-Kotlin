package com.mms.kotlin.spring.app.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import java.io.File

private var mapper: ObjectMapper = ObjectMapper();

fun getBodyFile(path: String): Map<*, *> {
    val resource: File = ClassPathResource(path).file;
    return mapper.readValue<Map<*, *>?>(resource.inputStream(), Map::class.java);
}