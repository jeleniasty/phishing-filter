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
    var id: Long = 0,

    @Column(name = "aggregate_id", nullable = false)
    var aggregateId: UUID,

    @Column(name = "topic", nullable = false)
    var topic: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: OutboxStatus = OutboxStatus.NEW
) : Auditable() {
    protected constructor() : this(
        id = 0,
        aggregateId = UUID.randomUUID(),
        topic = "",
        status = OutboxStatus.NEW
    )
}

