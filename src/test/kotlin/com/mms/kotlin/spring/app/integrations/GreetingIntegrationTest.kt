package com.mms.kotlin.spring.app.integrations

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GreetingIntegrationTest {

    @Autowired
    lateinit var client: WebTestClient;

    @Test
    fun testRetrieveGreeting() {
        client.get().uri("/v1/greetings/pedro").exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
            .expectBody(String::class.java)
            .isEqualTo("Hello pedro")
    }
}