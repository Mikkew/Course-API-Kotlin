package com.mms.kotlin.spring.app.integrations

import com.fasterxml.jackson.databind.ObjectMapper
import com.mms.kotlin.spring.app.data.DataUtils
import com.mms.kotlin.spring.app.utils.getBodyFile
import com.mms.kotlin.spring.app.models.dto.CourseDto
import mu.KLogger
import mu.KotlinLogging
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.stream.Stream


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = OrderAnnotation::class)
class CourseIntegrationTest {

    @Autowired
    private lateinit var client: WebTestClient;

    private lateinit var  mapper: ObjectMapper
    private lateinit var body_post: Map<*,*>;
    private lateinit var body_put: Map<*,*>;

    @BeforeEach
    fun setUp() {
        body_post = getBodyFile("request/course/post_course.json");
        body_put = getBodyFile("request/course/put_course.json");
        mapper = ObjectMapper();
    }

    @Test
    @Order(value = 1)
    fun testGetCourses() {
        client.get().uri("/course").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$[0].name").isEqualTo("Algebra")
            .jsonPath("$").isArray()
            .jsonPath("$.size()").isEqualTo(1)
            .jsonPath("$").value( hasSize<Any>(1))
            .jsonPath("$", hasSize<Any>(1))
    }

    @Test
    @Order(value = 2)
    fun testPostCourse() {
        client.post().uri("/instructor").contentType(MediaType.APPLICATION_JSON).bodyValue(DataUtils.INSTRUCTOR_DTO_002).exchange()
            .expectStatus().isCreated
            .expectHeader().contentType(MediaType.APPLICATION_JSON);

//        val resource: File = ClassPathResource("request/course/post_course.json").file;
//        val body: Map<*, *> = mapper.readValue<Map<*, *>?>(resource.inputStream(), Map::class.java)
//        val body: Map<*, *> = getBodyFile("request/course/post_course.json")

        client.post().uri("/course").contentType(MediaType.APPLICATION_JSON).bodyValue(body_post).exchange()
            .expectStatus().isCreated
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(CourseDto::class.java)
            .consumeWith { response ->
                run {
                    val courseResp: CourseDto = response.responseBody!!;
                    assertEquals(DataUtils.COURSE_DTO_002.category, courseResp.category);
                    assertEquals(DataUtils.COURSE_DTO_002.name, courseResp.name);
                    assertEquals(DataUtils.COURSE_DTO_002.id, courseResp.id);
                }
            };
    }

    @Test
    @Order(value = 3)
    fun testGetCourseById() {
        client.get().uri("/course/2").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value<Int>(`is`(DataUtils.COURSE_DTO_002.id!!.toInt()))
            .jsonPath("$.name").value<String>(`is`(DataUtils.COURSE_DTO_002.name))
//            .jsonPath("$.category").value(`is`<Any>("Ciencias Sociales"))
            .jsonPath("$.category").value<String>(`is`(DataUtils.COURSE_DTO_002.category))
//            .jsonPath("$.instructor.id").value<Int>(`is`(DataUtils.COURSE_001.instructor!!.id!!.toInt()))
            .jsonPath("$.instructor.id").value<Int>(`is`(2))
//            .jsonPath("$.instructor.name").value<String>(`is`(DataUtils.COURSE_001.name))
    }

    @Test
    @Order(value = 4)
    fun testUpdateCourse() {
//        var course: CourseDto = DataUtils.COURSE_002.copy();
//        course.name = "Calculus";

//        val resource: File = ClassPathResource("request/course/put_course.json").file;
//        val body: Map<*, *> = mapper.readValue<Map<*, *>?>(resource.inputStream(), Map::class.java);
//        val body: Map<*, *> = getBodyFile("request/course/put_course.json")
//        LOGGER.info { "$body" }

        client.put().uri("/course").bodyValue(body_put).exchange()
            .expectStatus().isAccepted
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value<Int>(`is`(body_put["id"]))
            .jsonPath("$.name").value<String>(`is`(body_put["name"]))
            .jsonPath("$.category").value<String>(`is`(body_put["category"]))
    }

    @Test
    @Order(value = 5)
    fun testDeleteCourse() {
//        val course_name: String = "Calculus";
//        val body: Map<*, *> = getBodyFile("request/course/put_course.json");

        client.get().uri("/course").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$", hasSize<Any>(2));

        client.delete().uri{uri -> uri.path("/course").path("/{id}").build(body_put["id"])}.exchange()
            .expectStatus().isAccepted
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value<Int>(`is`(body_put["id"]))
            .jsonPath("$.name").value<String>(`is`(body_put["name"]))
            .jsonPath("$.category").value<String>(`is`(body_put["category"]));

        client.get().uri("/course").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @ParameterizedTest
    @Order(value = 6)
    @MethodSource("provideParameters")
    fun testCourseByName(key: String, value: String) {
        client.get().uri{ uri -> uri.path("/course/filter")
                .queryParam(key, value)
                .build() }.exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$[0].name").value<String>(`is`(DataUtils.COURSE_DTO_001.name));
    }

    @ParameterizedTest
    @Order(value = 7)
    @ValueSource(strings = ["Math"])
    fun testCourseByCategory(value: String) {
        client.get().uri{ uri -> uri.path("/course/category")
                .path("/{category}")
                .build(value)
            }
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$[0].category").value(`is`<String>(value));
    }


    companion object {
        @JvmStatic
        public fun provideParameters() = Stream.of(
            Arguments.of("name", "Algebra")
        )

        private val LOGGER: KLogger = KotlinLogging.logger { }
    }
}