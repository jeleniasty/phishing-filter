package com.jeleniasty.phishingfilter.modules.delivery

import com.jeleniasty.phishingfilter.shared.model.OutboxStatus
import com.jeleniasty.phishingfilter.shared.service.MessageService
import com.jeleniasty.phishingfilter.shared.utils.logger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OutboxPublisher(
    private val kafkaTemplate: KafkaTemplate<UUID, PhishingEvent>,
    private val messageService: MessageService
) {
    private val logger = logger()

    @Transactional
    fun publish() {
        val events = messageService.getUnprocessedOutboxEvents()
        if (events.isEmpty()) return

        events.forEach { event ->
            logger.info("Unprocessed event: ${event.id}")
        }

        val messageIds = events.map { it.aggregateId }

        val messages = messageService.getMessages(messageIds).associateBy { it.id }

        events.forEach { event ->
            val message = messages[event.aggregateId]
                ?: throw IllegalStateException("Message ${event.aggregateId} not found")

            kafkaTemplate.send(
                event.topic, event.aggregateId,
                PhishingEvent(
                    messageId = message.id,
                    sender = message.sender,
                    recipient = message.recipient,
                    content = message.content
                )
            )
            logger.info("Message [messageId: {}] pushed to {} topic", event.aggregateId, event.topic)

            event.status = OutboxStatus.PROCESSED
        }

        messageService.saveOutbox(events)
    }
}