package com.jeleniasty.phishingfilter.shared.utils

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "message_log")
class MessageLog(
    @Id
    @Column(name = "message_id", nullable = false)
    val messageId: UUID = UUID.randomUUID(),

    @Column(name = "sender", length = 50)
    val sender: String? = null,

    @Column(name = "recipient", length = 50)
    val recipient: String? = null,

    @Column(name = "message", columnDefinition = "TEXT")
    val message: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now()
)

