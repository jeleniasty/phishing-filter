package com.jeleniasty.phishingfilter.modules.processing.model

import com.jeleniasty.phishingfilter.shared.model.PhishingStatus
import java.util.*

data class MessageOutDto(val messageId: UUID, val messageStatus: PhishingStatus)