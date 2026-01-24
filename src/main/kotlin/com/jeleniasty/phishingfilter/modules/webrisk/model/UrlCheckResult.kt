package com.jeleniasty.phishingfilter.modules.webrisk.model

data class UrlCheckResult(
    val riskyUrl: String? = null,
    val serviceError: Boolean = false
)
