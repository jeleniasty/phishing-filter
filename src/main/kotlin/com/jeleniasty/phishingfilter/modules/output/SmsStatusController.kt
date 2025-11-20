package com.jeleniasty.phishingfilter.modules.output

import com.jeleniasty.phishingfilter.modules.processing.model.MessageOutDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/sms")
class SmsStatusController(private val smsStatusService: SmsStatusService) {

    @GetMapping("{messageId}")
    fun getStatus(@PathVariable messageId: UUID): MessageOutDto = smsStatusService.getSmsStatus(messageId)

}