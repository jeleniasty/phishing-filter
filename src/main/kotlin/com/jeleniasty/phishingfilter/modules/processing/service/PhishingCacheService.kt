package com.jeleniasty.phishingfilter.modules.processing.service

import com.jeleniasty.phishingfilter.modules.processing.persistence.nosql.PhishingCacheRepository
import com.jeleniasty.phishingfilter.modules.processing.persistence.nosql.table.PhishingCache
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.cassandra.core.CassandraTemplate
import org.springframework.data.cassandra.core.InsertOptions
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class PhishingCacheService(
    @Value("\${cassandra.phishing-cache.ttl-days}") private val ttlDays: Long,
    private val repository: PhishingCacheRepository,
    private val cassandraTemplate: CassandraTemplate
) {

    fun saveKey(key: String) {
        val phishingCache = PhishingCache(key, Instant.now())
        val ttlDuration = Duration.ofDays(ttlDays)
        cassandraTemplate.insert(
            phishingCache,
            InsertOptions.builder().ttl(ttlDuration).build()
        )
    }

    fun exists(key: String): Boolean {
        return repository.existsByKey(key)
    }

    fun anyExists(keys: List<String>): Boolean {
        val existingKeys = repository.findAllById(keys).map { it.key }
        return existingKeys.isNotEmpty()
    }
}