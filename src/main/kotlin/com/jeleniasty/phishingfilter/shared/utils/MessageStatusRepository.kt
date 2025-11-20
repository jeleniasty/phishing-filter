package com.jeleniasty.phishingfilter.shared.utils

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MessageStatusRepository : JpaRepository<MessageStatus, UUID> {
}