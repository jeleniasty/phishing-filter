package com.jeleniasty.phishingfilter.modules.processing.model

import com.jeleniasty.phishingfilter.shared.utils.Status
import java.util.*

data class MessageOutDto(val messageId: UUID, val messageStatus: Status)