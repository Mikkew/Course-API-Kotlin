package com.mms.kotlin.spring.app.repositories

import com.mms.kotlin.spring.app.models.entities.Instructor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface IInstructorRepository: JpaRepository<Instructor, Long>, JpaSpecificationExecutor<Instructor> {
}