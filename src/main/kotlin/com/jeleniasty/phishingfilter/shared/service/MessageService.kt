package com.jeleniasty.phishingfilter.shared.service

import com.jeleniasty.phishingfilter.shared.model.OutboxStatus
import com.jeleniasty.phishingfilter.shared.persistence.message.Message
import com.jeleniasty.phishingfilter.shared.persistence.message.MessageRepository
import com.jeleniasty.phishingfilter.shared.persistence.outbox.Outbox
import com.jeleniasty.phishingfilter.shared.persistence.outbox.OutboxRepository
import com.jeleniasty.phishingfilter.shared.model.PhishingStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MessageService(
    @Value("\${kafka.topic.phishing-message-log.name}") private val phishingTopic: String,
    private val messageRepository: MessageRepository,
    private val outboxRepository: OutboxRepository
) {

    @Transactional
    fun saveMessage(sender: String, recipient: String, message: String): UUID {
        val messageId = UUID.randomUUID()

        messageRepository.save(
            Message(
                id = messageId,
                sender = sender,
                recipient = recipient,
                content = message,
                status = PhishingStatus.PENDING
            )
        )

        outboxRepository.save(
            Outbox(
                aggregateId = messageId,
                topic = phishingTopic,
                status = OutboxStatus.NEW
            )
        )

        return messageId
    }

    @Transactional(readOnly = true)
    fun getUnprocessedOutboxEvents(): List<Outbox> =
        outboxRepository.findTopByStatusOrderByCreatedAtSkipLocked(OutboxStatus.NEW.name, 10)

    @Transactional(readOnly = true)
    fun getMessages(messageIds: List<UUID>): List<Message> =
        messageRepository.findAllById(messageIds)

    fun getMessage(messageId: UUID): Optional<Message> =
        messageRepository.findById(messageId)

    @Transactional
    fun saveOutbox(outbox: List<Outbox>): List<Outbox> = outboxRepository.saveAll(outbox)

    @Transactional
    fun saveMessage(message: Message): Message = messageRepository.save(message)
}