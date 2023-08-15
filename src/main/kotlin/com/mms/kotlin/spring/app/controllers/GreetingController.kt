package com.mms.kotlin.spring.app.controllers

import com.mms.kotlin.spring.app.services.IGreetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/v1/greetings"])
class GreetingController {

    @Autowired
    private lateinit var greetingService: IGreetingService;

    @GetMapping(value = ["/{name}"])
    fun retrieveGreeting(@PathVariable name: String): String {
        return greetingService.retrieveGreeting(name);
    }
}