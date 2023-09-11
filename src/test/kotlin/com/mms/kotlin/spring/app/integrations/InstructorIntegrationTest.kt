package com.mms.kotlin.spring.app.integrations

import com.fasterxml.jackson.databind.ObjectMapper
import com.mms.kotlin.spring.app.data.DataUtils.Companion.INSTRUCTOR_DTO_001
import com.mms.kotlin.spring.app.utils.getBodyFile
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
class InstructorIntegrationTest {

    @Autowired
    private lateinit var client: WebTestClient;

    private lateinit var  mapper: ObjectMapper
    private lateinit var body_post: Map<*,*>;
    private lateinit var body_put: Map<*,*>;

    @BeforeEach
    fun setUp() {
        body_post = getBodyFile("request/instructor/post_instructor.json");
        body_put = getBodyFile("request/instructor/put_instructor.json");
        mapper = ObjectMapper();
    }

    @Test
    @Order(value = 1)
    fun testGetInstructors() {
        client.get().uri("/instructor").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$[0].id").value<Int>(`is`( INSTRUCTOR_DTO_001.id!!.toInt() ))
            .jsonPath("$[0].name").value<String>(`is`( INSTRUCTOR_DTO_001.name ))
            .jsonPath("$", hasSize<Any>(2))
    }

    @Test
    @Order(value = 2)
    fun testPostInstructor() {

        client.post().uri("/instructor").bodyValue(body_post).exchange()
            .expectStatus().isCreated
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value<Int>(`is`(2))
            .jsonPath("$.name").value<String> { `is`(body_post["name"])  };
    }

    @Test
    @Order(value = 3)
    fun testGetInstructorById() {
        client.get().uri("/instructor/2").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value<Int>(`is`(2))
            .jsonPath("$.name").value<String> { `is`(body_post["name"])  };
    }

    @Test
    @Order(value = 4)
    fun testPutInstructor() {
        client.put().uri("/instructor").bodyValue(body_put).exchange()
            .expectStatus().isAccepted
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value<Int>{ `is`(body_put["id"]) }
            .jsonPath("$.name").value<String>{ `is`(body_put["name"]) };
    }

    @Test
    @Order(value = 5)
    fun testDeleteInstructor() {
        client.get().uri("/instructor").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$", hasSize<Any>(2));

        client.delete().uri("/instructor/2").exchange()
            .expectStatus().isAccepted
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value<Int>(`is`(body_put["id"]))
            .jsonPath("$.name").value<String>(`is`(body_put["name"]))

        client.get().uri("/instructor/2").exchange()
            .expectStatus().isNotFound
            .expectHeader().contentType(MediaType.APPLICATION_JSON);

        client.get().uri("/instructor").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$", hasSize<Any>(1));
    }
}