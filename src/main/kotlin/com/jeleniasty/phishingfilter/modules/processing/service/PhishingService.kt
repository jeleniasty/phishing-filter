package com.jeleniasty.phishingfilter.modules.processing.service

import com.jeleniasty.phishingfilter.modules.webrisk.service.UrlValidationService
import com.jeleniasty.phishingfilter.modules.processing.model.MessageInDto
import com.jeleniasty.phishingfilter.modules.webrisk.service.UrlExtractorService
import com.jeleniasty.phishingfilter.shared.utils.MessageService
import com.jeleniasty.phishingfilter.shared.utils.Status
import com.jeleniasty.phishingfilter.shared.utils.logger
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
    private val phishingCacheService: PhishingCacheService
) {
    private val logger = logger()

    @KafkaListener(
        topics = ["\${kafka.topic.phishing-message-log.name}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun check(
        @Header(KafkaHeaders.RECEIVED_KEY) key: UUID,
        dto: MessageInDto
    ) {
        logger.info("Received message [messageId:{}]. Processing...", dto.recipient)

        if (subscriptionService.processSubscription(dto.sender, dto.message)) {
            updateMessageStatus(key, Status.SKIPPED)
            return
        }

        if (!subscriptionService.isSubscribed(dto.recipient)) {
            logger.info("Recipient {} is not subscribed. Skipping processing", dto.recipient)
            updateMessageStatus(key, Status.SKIPPED)
            return
        }

        if (phishingCacheService.exists(dto.sender)) {
            logger.info("Sender {} is on the blacklist. Returning PHISHING", dto.sender)
            updateMessageStatus(key, Status.PHISHING)
            return
        }

        val urls = UrlExtractorService.extractValidUrls(dto.message)
        if (urls.isEmpty()) {
            updateMessageStatus(key, Status.SAFE)
            logger.info("No url found in message. Message is SAFE")
            return
        }

        logger.info("Found urls: {}", urls)

        if (phishingCacheService.anyExists(urls)) {
            logger.info("One of urls [{}] is on the blacklist. Returning PHISHING", urls)
            updateMessageStatus(key, Status.PHISHING)
            return
        }


        urlValidationService.findRiskyUrl(urls).subscribe { result ->
            when {
                result.serviceError -> {
                    logger.warn("Could not evaluate URLs {}, marking as PENDING", urls)
                    updateMessageStatus(key, Status.PENDING)
                }

                result.riskyUrl != null -> {
                    logger.info("One of urls {} evaluated as malicious. Returning PHISHING", urls)
                    phishingCacheService.saveKey(result.riskyUrl)
                    phishingCacheService.saveKey(dto.sender)
                    updateMessageStatus(key, Status.PHISHING)
                }
            }
        }

        updateMessageStatus(key, Status.SAFE)
        logger.info("Message [messageId: {}] processed", key)
    }


    private fun updateMessageStatus(messageId: UUID, newStatus: Status) =
        messageService.getMessageStatus(messageId)
            .orElseThrow { IllegalArgumentException("Message [messageId: $messageId] not found") }
            .apply { status = newStatus }
            .let { messageService.saveStatus((it)) }
}