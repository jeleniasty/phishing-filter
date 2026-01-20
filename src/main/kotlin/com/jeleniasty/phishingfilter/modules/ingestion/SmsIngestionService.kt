package com.jeleniasty.phishingfilter.modules.ingestion

import com.jeleniasty.phishingfilter.modules.processing.model.MessageInDto
import com.jeleniasty.phishingfilter.modules.processing.model.MessageOutDto
import com.jeleniasty.phishingfilter.shared.model.OutboxStatus
import com.jeleniasty.phishingfilter.shared.service.MessageService
import com.jeleniasty.phishingfilter.shared.model.PhishingStatus
import com.jeleniasty.phishingfilter.shared.utils.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class SmsIngestionService(
    private val messageService: MessageService
) {

    private val logger = logger()

    fun ingestMessage(dto: MessageInDto): MessageOutDto {
        val messageId = messageService.saveMessage(dto.sender, dto. recipient, dto.message)
        logger.info("Message [messageId: {}] saved", messageId)


        return MessageOutDto(messageId, PhishingStatus.PENDING)
    }
}