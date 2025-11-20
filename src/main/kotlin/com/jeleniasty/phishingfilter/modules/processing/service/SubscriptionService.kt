package com.jeleniasty.phishingfilter.modules.processing.service

import com.jeleniasty.phishingfilter.modules.processing.model.SubscriptionActionType
import com.jeleniasty.phishingfilter.modules.processing.persistence.SubscriptionRepository
import com.jeleniasty.phishingfilter.modules.processing.persistence.entity.Subscription
import com.jeleniasty.phishingfilter.shared.utils.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubscriptionService(private val subscriptionRepository: SubscriptionRepository) {

    private val logger = logger()

    @Transactional
    fun processSubscription(sender: String, message: String): Boolean {
        return when (SubscriptionActionType.fromMessage(message)) {
            SubscriptionActionType.START -> {
                subscribe(sender)
                false
            }

            SubscriptionActionType.STOP -> {
                unsubscribe(sender)
                true
            }

            null -> false
        }
    }

    fun subscribe(subscriber: String) {
        subscriptionRepository.findBySubscriber(subscriber)?.let {
            it.enabled = true
            subscriptionRepository.save(it)
        } ?: run {
            subscriptionRepository.save(Subscription(subscriber = subscriber, enabled = true))
        }

        logger.info("Subscriber [{}] successfully subscribed", subscriber)
    }

    fun unsubscribe(subscriber: String) {
        subscriptionRepository.findBySubscriber(subscriber)?.let {
            it.enabled = false
            subscriptionRepository.save(it)
        }


        logger.info("Subscriber [{}] successfully unsubscribed", subscriber)
    }

    fun isSubscribed(subscriber: String): Boolean =
        subscriptionRepository.findBySubscriber(subscriber)?.enabled?: false
}