package com.jeleniasty.phishingfilter.modules.output

import com.jeleniasty.phishingfilter.modules.processing.model.MessageOutDto
import com.jeleniasty.phishingfilter.shared.service.MessageService
import com.jeleniasty.phishingfilter.shared.model.PhishingStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class SmsStatusService(private val messageService: MessageService) {

    fun getSmsStatus(messageId: UUID): MessageOutDto {
        val phishingStatus = messageService.getMessage(messageId)
            .map { it.status }
            .orElse(PhishingStatus.UNKNOWN)
        return MessageOutDto(messageId, phishingStatus)
    }

}