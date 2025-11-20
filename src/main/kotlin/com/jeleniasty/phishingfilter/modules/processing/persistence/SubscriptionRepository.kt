package com.jeleniasty.phishingfilter.modules.processing.persistence

import com.jeleniasty.phishingfilter.modules.processing.persistence.entity.Subscription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository : JpaRepository<Subscription, Long> {
    fun findBySubscriber(sender: String): Subscription?
}