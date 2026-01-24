package com.jeleniasty.phishingfilter.modules.delivery

import java.util.*

data class PhishingEvent(
    val messageId: UUID,
    val sender: String,
    val recipient: String,
    val content: String
)
