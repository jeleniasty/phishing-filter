package com.jeleniasty.phishingfilter.modules.webrisk.model

data class UriWebRiskRequest(val uri: String, val threatTypes: List<ThreatType>, val allowScan: Boolean)