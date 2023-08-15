package com.mms.kotlin.spring.app.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mms.kotlin.spring.app.controllers.GreetingController
import com.mms.kotlin.spring.app.services.impl.GreetingServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc

import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(GreetingController::class)
class GreetingControllerTest {

    @MockBean
    private lateinit var service: GreetingServiceImpl;

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper();
    }

    @Test
    fun testRetrieveGrettings() {
        val name = "Pedro"
        `when`(service.retrieveGreeting(anyString())).thenReturn("Hello $name")

        mockMvc.perform(get("/v1/greetings/pedro"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
            .andExpect(content().string("Hello $name"))
    }

}