package com.jeleniasty.phishingfilter.modules.webrisk.model

data class UriWebRiskResponse(
    val scores: List<Score>
)

data class Score(
    val threatType: ThreatType,
    val confidenceLevel: ConfidenceLevel
)

enum class ConfidenceLevel {
    CONFIDENCE_LEVEL_UNSPECIFIED,
    SAFE,
    LOW,
    MEDIUM,
    HIGH,
    HIGHER,
    VERY_HIGH,
    EXTREMELY_HIGH
}

