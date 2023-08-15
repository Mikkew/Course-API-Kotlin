package com.mms.kotlin.spring.app.models.entities

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "COURSES")
data class Course(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", updatable = false, nullable = false)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String?,

    @Column(name = "category", nullable = false)
    var category: String?,

//    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @ManyToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    @JoinColumn(
        name = "instructor_id",
        referencedColumnName = "instructor_id",
        nullable = true,
        foreignKey = ForeignKey(name = "fk_course_instructor"))
    var instructor: Instructor?,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
) {
    constructor(name: String, category: String, instructor: Instructor?): this(null, name, category, instructor)

    private constructor(builder: Course.Builder) : this(
        builder.id,
        builder.name,
        builder.category,
        builder.instructor
    )

    class Builder {
        var id: Long? = null
            private set
        var name: String? = null
            private set
        var category: String? = null
            private set
        var instructor: Instructor? = null
            private set

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun category(category: String) = apply { this.category = category }
        fun instructor(instructor: Instructor) = apply { this.instructor = instructor }
        fun build() = Course(this)
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
        return "Course(id=$id, name=$name, category=$category, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}