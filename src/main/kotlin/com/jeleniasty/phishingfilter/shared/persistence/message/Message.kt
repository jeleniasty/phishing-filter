package com.jeleniasty.phishingfilter.shared.persistence.message

import com.jeleniasty.phishingfilter.shared.model.PhishingStatus
import com.jeleniasty.phishingfilter.shared.persistence.Auditable
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "message")
open class Message(
    @Id
    open var id: UUID,

    open var sender: String,

    open var recipient: String,

    open var content: String,

    @Enumerated(EnumType.STRING)
    open var status: PhishingStatus = PhishingStatus.UNKNOWN,

    ) : Auditable() {
    protected constructor() : this(
        id = UUID.randomUUID(),
        sender = "",
        recipient = "",
        content = "",
        status = PhishingStatus.UNKNOWN
    )
}

