package com.jeleniasty.phishingfilter.shared.persistence.outbox

import com.jeleniasty.phishingfilter.shared.model.OutboxStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OutboxRepository : JpaRepository<Outbox, Long> {
    fun findTop100ByStatusOrderByCreatedAt(status: OutboxStatus): List<Outbox>
}