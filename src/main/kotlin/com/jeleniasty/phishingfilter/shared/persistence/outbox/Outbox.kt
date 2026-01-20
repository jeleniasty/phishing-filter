package com.jeleniasty.phishingfilter.shared.persistence.outbox

import com.jeleniasty.phishingfilter.shared.model.OutboxStatus
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "outbox",
)
open class Outbox(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "aggregate_id", nullable = false)
    val aggregateId: UUID,

    @Column(name = "topic", nullable = false)
    val topic: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: OutboxStatus = OutboxStatus.NEW,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now()
) {
    protected constructor() : this(
        id = 0,
        aggregateId = UUID.randomUUID(),
        topic = "",
        status = OutboxStatus.NEW,
        createdAt = Instant.now()
    )
}

