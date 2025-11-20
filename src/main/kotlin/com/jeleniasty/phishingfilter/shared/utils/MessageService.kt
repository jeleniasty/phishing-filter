package com.jeleniasty.phishingfilter.shared.utils

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MessageService(
    private val messageLogRepository: MessageLogRepository,
    private val messageStatusRepository: MessageStatusRepository
) {

    @Transactional
    fun saveMessage(sender: String, recipient: String, message: String): UUID {
        val messageId = UUID.randomUUID()

        messageLogRepository.save(
            MessageLog(
                messageId = messageId,
                sender = sender,
                recipient = recipient,
                message = message
            )
        )

        messageStatusRepository.save(
            MessageStatus(
                messageId = messageId,
                status = Status.PROCESSING
            )
        )

        return messageId
    }

    fun getMessageStatus(messageId: UUID): Optional<MessageStatus> =
        messageStatusRepository.findById(messageId)

    @Transactional
    fun saveStatus(messageStatus: MessageStatus) = messageStatusRepository.save(messageStatus)
}