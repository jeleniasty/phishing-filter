package com.jeleniasty.phishingfilter.modules.processing.service

import com.jeleniasty.phishingfilter.modules.webrisk.service.UrlValidationService
import com.jeleniasty.phishingfilter.modules.processing.model.MessageInDto
import com.jeleniasty.phishingfilter.modules.webrisk.service.UrlExtractorService
import com.jeleniasty.phishingfilter.shared.utils.MessageService
import com.jeleniasty.phishingfilter.shared.utils.Status
import com.jeleniasty.phishingfilter.shared.utils.logger
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import java.util.*

@Service
class PhishingService(
    private val subscriptionService: SubscriptionService,
    private val urlValidationService: UrlValidationService,
    private val messageService: MessageService,
    private val phishingTopic: NewTopic
) {
    private val logger = logger();

    @KafkaListener(topics = ["phishing-message"])
    fun logAllMessages(msg: String) {
        logger.info("Got message: $msg")
    }

    @KafkaListener(
        topics = ["\${kafka.topic.phishing-message-log.name}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun check(
        @Header(KafkaHeaders.RECEIVED_KEY) key: UUID,
        dto: MessageInDto
    ) {
        logger.info("Received message [messageId:{}]. Processing...", dto.recipient)

        if (subscriptionService.processSubscription(dto.sender, dto.message)) return

        if (!subscriptionService.isSubscribed(dto.recipient)) {
            logger.info("Recipient {} is not subscribed, skipping processing", dto.recipient)
            return
        }

        val urls = UrlExtractorService.extractValidUrls(dto.message)
        if (urls.isEmpty()) {
            logger.info("No url found in message. Skipping processing")
            return
        }

        //TODO cache
        //check if sender is already blocked
        //check if url is already blocked

//        urlValidationService.evaluateUrls(urls)

        updateMessageStatus(key, Status.SAFE)
            logger.info("Message [messageId: {}] processed", key)
        }


    private fun updateMessageStatus(messageId: UUID, newStatus: Status) =
        messageService.getMessageStatus(messageId)
            .orElseThrow { IllegalArgumentException("Message [messageId: $messageId] not found") }
            .apply { status = newStatus }
            .let { messageService.saveStatus((it)) }
}