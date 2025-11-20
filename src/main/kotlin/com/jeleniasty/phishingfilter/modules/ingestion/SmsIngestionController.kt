package com.jeleniasty.phishingfilter.modules.ingestion

import com.jeleniasty.phishingfilter.modules.processing.model.MessageInDto
import com.jeleniasty.phishingfilter.modules.processing.model.MessageOutDto
import com.jeleniasty.phishingfilter.modules.processing.service.PhishingService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/sms")
class SmsIngestionController(private val smsIngestionService: SmsIngestionService) {

    @PostMapping
    fun ingestMessage(@RequestBody dto: MessageInDto): MessageOutDto = smsIngestionService.ingestMessage(dto)

}