package com.jeleniasty.phishingfilter.modules.processing.persistence.sql.entity

import com.jeleniasty.phishingfilter.shared.persistence.Auditable
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
    var id: Long? = null,

    @Column(name = "subscriber", nullable = false)
    var subscriber: String,

    @Column(nullable = false)
    var enabled: Boolean = true
) : Auditable() {
    constructor() : this(0, "")
}
