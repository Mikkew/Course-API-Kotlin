package com.mms.kotlin.spring.app.controllers

import com.mms.kotlin.spring.app.models.dto.InstructorDto
import com.mms.kotlin.spring.app.services.IInstructorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping(value = ["/instructor"])
class InstructorController {

    @Autowired
    private lateinit var service: IInstructorService;

    @GetMapping(value = ["", "/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.OK)
    fun getInstructors(): ResponseEntity<Any>? {
        val response: List<InstructorDto> = service.getInstructors();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.OK)
    fun getInstructorById(@PathVariable id: Long): ResponseEntity<Any>? {
        val response: InstructorDto = service.getInstructorById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = ["", "/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.CREATED)
    fun postInstructor(@RequestBody body:InstructorDto): ResponseEntity<Any>? {
        val response: InstructorDto = service.createInstructor(body);
        val uri: URI = MvcUriComponentsBuilder.fromController(this::class.java).path("/{id}").buildAndExpand(response.id).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = ["", "/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    fun putInstructor(@RequestBody body:InstructorDto): ResponseEntity<Any> {
        val response: InstructorDto = service.updateInstructor(body);
        return ResponseEntity.accepted().body(response);
    }

    @DeleteMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    fun deleteInstructor(@PathVariable id: Long): ResponseEntity<Any>? {
        val response:InstructorDto = service.deleteInstructor(id);
        return ResponseEntity.accepted().body(response);
    }
}