package com.mms.kotlin.spring.app.services

interface IGreetingService {

    fun retrieveGreeting(name: String): String;
}