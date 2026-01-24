package com.jeleniasty.phishingfilter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class PhishingFilterApplication

fun main(args: Array<String>) {
    runApplication<PhishingFilterApplication>(*args)
}
