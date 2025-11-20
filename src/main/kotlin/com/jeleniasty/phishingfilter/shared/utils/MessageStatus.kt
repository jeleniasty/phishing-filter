package com.jeleniasty.phishingfilter.shared.utils

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "message_status")
class MessageStatus(
    @Id
    @Column(name = "message_id", nullable = false)
    val messageId: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: Status = Status.PROCESSING,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now()
)

