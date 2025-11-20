package com.jeleniasty.phishingfilter.modules.webrisk.service

import com.jeleniasty.phishingfilter.modules.webrisk.model.ConfidenceLevel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class UrlValidationService(
    private val webRiskClient: WebRiskClient,
    @Value("\${web-risk.confidence-threshold:SAFE}") private val confidenceThresholdStr: String
) {

    private val confidenceThreshold = ConfidenceLevel.valueOf(confidenceThresholdStr)

    fun findFindRiskyUrl(urls: List<String>): String? {
        for (url in urls) {
            val response = webRiskClient.checkUri(url).block() ?: continue

            if (response.scores.any { it.confidenceLevel.isHigherThan(confidenceThreshold) }) {
                return url
            }
        }
        return null
    }

    fun ConfidenceLevel.isHigherThan(other: ConfidenceLevel): Boolean {
        return this.ordinal > other.ordinal
    }
}

