package com.jeleniasty.phishingfilter.modules.processing.persistence.sql.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "subscription",
    uniqueConstraints = [UniqueConstraint(name = "uk_subscriber", columnNames = ["subscriber"])]
)
class Subscription(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "subscriber", nullable = false)
    val subscriber: String,

    @Column(nullable = false)
    var enabled: Boolean = true,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(0, "")

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
