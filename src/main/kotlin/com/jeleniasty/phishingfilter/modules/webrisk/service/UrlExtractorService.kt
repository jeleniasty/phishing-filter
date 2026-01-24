package com.jeleniasty.phishingfilter.modules.webrisk.service

import com.linkedin.urls.detection.UrlDetector
import com.linkedin.urls.detection.UrlDetectorOptions
import org.apache.commons.validator.routines.UrlValidator

internal class UrlExtractorService {
    companion object {
        private val validator = UrlValidator(arrayOf("http", "https"))

        fun extractValidUrls(text: String): List<String> {
            val detector = UrlDetector(text, UrlDetectorOptions.Default)
            return detector.detect()
                .map { it.fullUrl }
                .filter { validator.isValid(it) }
        }
    }
}