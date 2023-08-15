package com.mms.kotlin.spring.app.models.error

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

class WrapperError<T> (
    val status: Int?,

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss a", timezone = "America/Mexico_City")
    val time: LocalDateTime?,

    val errors: T
) {

    fun createResponse(status: HttpStatus): ResponseEntity<Any> {
        return ResponseEntity.status(status).body<Any>(this)
    }
}