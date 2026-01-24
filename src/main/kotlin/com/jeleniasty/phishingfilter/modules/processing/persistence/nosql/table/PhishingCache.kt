package com.jeleniasty.phishingfilter.modules.processing.persistence.nosql.table

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.Instant

@Table("phishing_cache")
data class PhishingCache(
    @PrimaryKey
    val key: String,
    val createdAt: Instant = Instant.now()
)
