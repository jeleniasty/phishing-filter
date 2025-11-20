package com.jeleniasty.phishingfilter.modules.processing.persistence.nosql

import com.jeleniasty.phishingfilter.modules.processing.persistence.nosql.table.PhishingCache
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface PhishingCacheRepository : CassandraRepository<PhishingCache, String> {
    fun existsByKey(key: String): Boolean
}