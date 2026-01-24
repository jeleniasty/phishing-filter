package com.jeleniasty.phishingfilter.modules.webrisk.service

import com.jeleniasty.phishingfilter.modules.webrisk.model.ThreatType
import com.jeleniasty.phishingfilter.modules.webrisk.model.UriWebRiskRequest
import com.jeleniasty.phishingfilter.modules.webrisk.model.UriWebRiskResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
class WebRiskClient(
    private val webClient: WebClient,
    @Value("\${google.api-key}") private val apiKey: String
) {

    fun checkUri(uri: String): Mono<UriWebRiskResponse> {
        val url = "https://webrisk.googleapis.com/v1eap1:evaluateUri?key=$apiKey"
        val requestBody = UriWebRiskRequest(uri, ThreatType.entries, true)

        return webClient.post()
            .uri(url)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono()
    }
}
