package com.jeleniasty.phishingfilter.modules.ingestion

import com.jeleniasty.phishingfilter.modules.processing.model.MessageInDto
import com.jeleniasty.phishingfilter.modules.processing.model.MessageOutDto
import com.jeleniasty.phishingfilter.shared.utils.MessageService
import com.jeleniasty.phishingfilter.shared.utils.Status
import com.jeleniasty.phishingfilter.shared.utils.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class SmsIngestionService(
    @Value("\${kafka.topic.phishing-message-log.name}") private val phishingTopic: String,
    private val messageService: MessageService,
    private val kafkaTemplate: KafkaTemplate<UUID, MessageInDto>) {

    private val logger = logger()

    fun ingestMessage(dto: MessageInDto): MessageOutDto {
        val messageId = messageService.saveMessage(dto.sender, dto.recipient, dto.message)
        logger.info("Message [messageId: {}] saved", messageId)

        // ✅ FIX: Add callback to handle success/failure
        val sendResult = kafkaTemplate.send(phishingTopic, messageId, dto)

        sendResult.whenComplete { result, ex ->
            if (ex != null) {
                // ❌ Handle error
                logger.error("Failed to send message [messageId: {}] to Kafka topic: {}",
                    messageId, ex.message, ex)
                // TODO: Update message status to FAILED if needed
            } else {
                // ✅ Success
                logger.info("Message [messageId: {}] pushed to {} topic successfully. Partition: {}, Offset: {}",
                    messageId, phishingTopic,
                    result?.recordMetadata?.partition(),
                    result?.recordMetadata?.offset())
            }
        }

        return MessageOutDto(messageId, Status.PROCESSING)
    }
}