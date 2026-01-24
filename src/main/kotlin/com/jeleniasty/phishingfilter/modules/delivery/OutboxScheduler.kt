package com.jeleniasty.phishingfilter.modules.delivery

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class OutboxScheduler(
    private val outboxPublisher: OutboxPublisher
) {

    @Scheduled(fixedDelay = 1000)
    fun run() {
        outboxPublisher.publish()
    }
}
