package com.mms.kotlin.spring.app.controllers

import com.mms.kotlin.spring.app.models.dto.CourseDto
import com.mms.kotlin.spring.app.models.entities.Course
import com.mms.kotlin.spring.app.models.error.WrapperError
import com.mms.kotlin.spring.app.services.ICourseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import mu.KLogger
import mu.KotlinLogging
import net.kaczmarzyk.spring.data.jpa.domain.Like
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.Specification
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
@RequestMapping(value = ["/course"])
class CourseController @Autowired constructor(private var service: ICourseService) {

    companion object {
        private val LOGGER: KLogger = KotlinLogging.logger { }
    }

//    private lateinit var service: ICourseService;
//
//    @Autowired
//    constructor(service: ICourseService) {
//        this.service = service
//    }

//    @Autowired
//    fun init(service: ICourseService) {
//        this.service = service
//    }

    @Operation(summary = "Get a list courses")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found list of courses", content = [Content(mediaType = "application/json", array = ArraySchema( schema = Schema(implementation = CourseDto::class)))]),
        ApiResponse(responseCode = "404", description = "Not Found of courses", content = [Content(mediaType = "application/json", schema = Schema(implementation = WrapperError::class))])
    ])
    @GetMapping(value = ["", "/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.OK)
    fun getCourses(): ResponseEntity<Any>? {
        val response: List<CourseDto> = service.getCourses();
        LOGGER.info { "$response" }
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = ["/filter"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.OK)
    fun getCoursesFiltered(@Spec(path = "name", spec = Like::class) spec: Specification<Course>): ResponseEntity<Any>? {
        var response: List<CourseDto> = service.getCoursesFilters(spec);
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found the course", content = [Content(mediaType = "application/json", schema = Schema(implementation = CourseDto::class))]),
        ApiResponse(responseCode = "404", description = "Invalid id supplied", content = [Content(mediaType = "application/json", schema = Schema(implementation = WrapperError::class))])
    ])
    @GetMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.OK)
    fun getCourse(@PathVariable id: Long): ResponseEntity<Any> {
        var response: CourseDto = service.getCourse(id);
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found list of courses", content = [Content(mediaType = "application/json", array = ArraySchema( schema = Schema(implementation = CourseDto::class)))]),
        ApiResponse(responseCode = "404", description = "Not Found of courses", content = [Content(mediaType = "application/json", schema = Schema(implementation = WrapperError::class))])
    ])
    @GetMapping(value = ["/category/{category}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.OK)
    fun getCoursesByCategory(@Parameter(description = "id of course to be searched") @PathVariable category: String): ResponseEntity<Any> {
        var response: List<CourseDto> = service.getCoursesByCategory(category);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = ["", "/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.CREATED)
    fun postCourse(@Valid @RequestBody body: CourseDto): ResponseEntity<Any> {
        var response: CourseDto = service.createCourse(body);
        val uri: URI = MvcUriComponentsBuilder.fromController(this::class.java).path("/{id}").buildAndExpand(response.id)
            .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = ["", "/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    fun putCourse(@RequestBody body: CourseDto): ResponseEntity<Any> {
        var response: CourseDto = service.updateCourse(body);
        return ResponseEntity.accepted().body(response);
    }

    @DeleteMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    fun deleteCourse(@PathVariable id: Long): ResponseEntity<Any> {
        var response: CourseDto = service.deleteCourse(id);
        return ResponseEntity.accepted().body(response);
    }

}