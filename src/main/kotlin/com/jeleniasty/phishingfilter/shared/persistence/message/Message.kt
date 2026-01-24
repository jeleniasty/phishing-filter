package com.jeleniasty.phishingfilter.shared.persistence.message

import com.jeleniasty.phishingfilter.shared.model.PhishingStatus
import com.jeleniasty.phishingfilter.shared.persistence.Auditable
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "message")
open class Message(
    @Id
    var id: UUID,

    var sender: String,

    var recipient: String,

    var content: String,

    @Enumerated(EnumType.STRING)
    var status: PhishingStatus = PhishingStatus.UNKNOWN,

    ) : Auditable() {
    protected constructor() : this(
        id = UUID.randomUUID(),
        sender = "",
        recipient = "",
        content = "",
        status = PhishingStatus.UNKNOWN
    )
}

