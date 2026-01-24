package com.jeleniasty.phishingfilter.shared.persistence.outbox

import com.jeleniasty.phishingfilter.shared.model.OutboxStatus
import com.jeleniasty.phishingfilter.shared.persistence.Auditable
import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "outbox",
)
open class Outbox(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long = 0,

    @Column(name = "aggregate_id", nullable = false)
    open var aggregateId: UUID,

    @Column(name = "topic", nullable = false)
    open var topic: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    open var status: OutboxStatus = OutboxStatus.NEW
) : Auditable() {
    protected constructor() : this(
        id = 0,
        aggregateId = UUID.randomUUID(),
        topic = "",
        status = OutboxStatus.NEW
    )
}

