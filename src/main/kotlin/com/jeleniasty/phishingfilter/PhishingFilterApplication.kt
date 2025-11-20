package com.jeleniasty.phishingfilter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PhishingFilterApplication

fun main(args: Array<String>) {
    runApplication<PhishingFilterApplication>(*args)
}
