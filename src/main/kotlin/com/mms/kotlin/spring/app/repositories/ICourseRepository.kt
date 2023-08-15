package com.mms.kotlin.spring.app.repositories

import com.mms.kotlin.spring.app.models.entities.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ICourseRepository: JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    fun findByCategoryContains(category: String): Optional<List<Course>>;
}