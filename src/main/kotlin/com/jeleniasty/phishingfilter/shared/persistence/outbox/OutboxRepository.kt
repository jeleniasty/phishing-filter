package com.jeleniasty.phishingfilter.shared.persistence.outbox

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface OutboxRepository : JpaRepository<Outbox, Long> {
    @Query(
        value = """
        SELECT * FROM outbox 
        WHERE status = :status 
        ORDER BY created_at 
        LIMIT :limit 
        FOR UPDATE SKIP LOCKED
    """, nativeQuery = true
    )
    fun findTopByStatusOrderByCreatedAtSkipLocked(
        @Param("status") status: String,
        @Param("limit") limit: Int
    ): List<Outbox>
}