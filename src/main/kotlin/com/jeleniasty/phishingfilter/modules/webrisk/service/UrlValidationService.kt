package com.jeleniasty.phishingfilter.modules.webrisk.service

import com.jeleniasty.phishingfilter.modules.webrisk.model.ConfidenceLevel
import com.jeleniasty.phishingfilter.modules.webrisk.model.UrlCheckResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UrlValidationService(
    private val webRiskClient: WebRiskClient,
    @Value("\${web-risk.confidence-threshold:SAFE}") private val confidenceThresholdStr: String
) {

    private val confidenceThreshold = ConfidenceLevel.valueOf(confidenceThresholdStr)

    fun findRiskyUrl(urls: List<String>): Mono<UrlCheckResult> {
        return Flux.fromIterable(urls)
            .concatMap { url ->
                webRiskClient.checkUri(url)
                    .map { response ->
                        UrlCheckResult(
                            riskyUrl = if (response.scores.any { it.confidenceLevel.isHigherThan(confidenceThreshold) }) url else null
                        )
                    }
                    .onErrorResume {
                        Mono.just(UrlCheckResult(serviceError = true))
                    }
            }
            .filter { it.riskyUrl != null || it.serviceError }
            .next()
            .defaultIfEmpty(UrlCheckResult())
    }


    fun ConfidenceLevel.isHigherThan(other: ConfidenceLevel): Boolean {
        return this.ordinal > other.ordinal
    }
}

