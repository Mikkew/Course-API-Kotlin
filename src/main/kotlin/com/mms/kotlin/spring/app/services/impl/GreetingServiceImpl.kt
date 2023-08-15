package com.mms.kotlin.spring.app.services.impl

//import io.github.oshai.kotlinlogging.KotlinLogging
//import com.mms.kotlin.spring.app.utils.LOGGER
import com.mms.kotlin.spring.app.services.IGreetingService
import mu.KotlinLogging
//import io.klogging.NoCoLogger
//import io.klogging.java.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class GreetingServiceImpl: IGreetingService {

//    companion object : KLogging()
    private var LOGGER = KotlinLogging.logger { }

//    private val LOGGER: NoCoLogger = LoggerFactory.getLogger(GreetingServiceImpl::class.java);

    @Value("\${message}")
    lateinit var message: String;

    override fun retrieveGreeting(name: String): String {
        LOGGER.info { "Retrieving $message" }
        return "Hello $name";
    }
}