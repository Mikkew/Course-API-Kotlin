package com.mms.kotlin.spring.app.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(title = "Kotlin API Course", version = "1.0", description = "Course Information")
)
class SwaggerConfig