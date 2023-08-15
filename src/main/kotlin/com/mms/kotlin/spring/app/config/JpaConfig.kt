package com.mms.kotlin.spring.app.config

import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableTransactionManagement
class JpaConfig: WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(SpecificationArgumentResolver())
    }
}