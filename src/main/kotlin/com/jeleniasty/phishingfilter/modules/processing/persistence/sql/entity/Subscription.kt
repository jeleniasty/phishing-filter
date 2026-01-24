package com.jeleniasty.phishingfilter.modules.processing.persistence.sql.entity

import com.jeleniasty.phishingfilter.shared.persistence.Auditable
import jakarta.persistence.*

@Entity
@Table(
    name = "subscription",
    uniqueConstraints = [UniqueConstraint(name = "uk_subscriber", columnNames = ["subscriber"])]
)
open class Subscription(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(name = "subscriber", nullable = false)
    open var subscriber: String,

    @Column(nullable = false)
    open var enabled: Boolean = true
) : Auditable() {
    constructor() : this(0, "")
}
