package com.jeleniasty.phishingfilter.modules.output

import com.jeleniasty.phishingfilter.modules.processing.model.MessageOutDto
import com.jeleniasty.phishingfilter.shared.utils.MessageService
import com.jeleniasty.phishingfilter.shared.utils.Status
import org.springframework.stereotype.Service
import java.util.*

@Service
class SmsStatusService(private val messageService: MessageService) {

    fun getSmsStatus(messageId: UUID): MessageOutDto {
        val status = messageService.getMessageStatus(messageId)
            .map { it.status }
            .orElse(Status.UNKNOWN)
        return MessageOutDto(messageId, status)
    }

}