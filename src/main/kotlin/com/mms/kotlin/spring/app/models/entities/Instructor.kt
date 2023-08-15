package com.mms.kotlin.spring.app.models.entities

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "INSTRUCTORS")
data class Instructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id", updatable = false, nullable = false)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String?,

    @OneToMany(
        cascade = [CascadeType.ALL],
        mappedBy = "instructor"
    )
    var courses: List<Course>?,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
) {
    constructor(name: String, courses: List<Course>): this(null, name, courses);


    private constructor(builder: Instructor.Builder) : this(
        builder.id,
        builder.name,
        builder.courses,
    )

    class Builder {
        var id: Long? = null
            private set
        var name: String? = null
            private set
        var courses: List<Course>? = null
            private set

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun courses(courses: List<Course>) = apply { this.courses = courses }
        fun build() = Instructor(this)
    }

    @PrePersist
    protected fun prePersist(): Unit {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected fun preUpdate(): Unit {
        updatedAt = LocalDateTime.now();
    }

    override fun toString(): String {
        return "Instructor(id=$id, name=$name, createdAt=$createdAt, updatedAt=$updatedAt)"
    }


}