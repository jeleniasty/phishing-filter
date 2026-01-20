package com.jeleniasty.phishingfilter.shared.persistence.message

import com.jeleniasty.phishingfilter.shared.model.PhishingStatus
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "message")
open class Message(
    @Id
    val id: UUID,

    val sender: String,
    val recipient: String,
    val content: String,

    @Enumerated(EnumType.STRING)
    var status: PhishingStatus = PhishingStatus.UNKNOWN,

    @Column(updatable = false)
    val createdAt: Instant = Instant.now(),

    var updatedAt: Instant = Instant.now()
) {
    protected constructor() : this(
        id = UUID.randomUUID(),
        sender = "",
        recipient = "",
        content = "",
        status = PhishingStatus.UNKNOWN,
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )
}

