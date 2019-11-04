package com.kortov.bootygram.bots

import com.kortov.bootygram.config.TelegramProperties
import mu.KLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.objects.Update

@RestController("/")
class HelloController(val myAmazingBot: HelloBot) {

    @PostMapping(TelegramProperties.WEB_HOOK)
    fun index(@RequestBody update: Update) {
        myAmazingBot.onWebhookUpdateReceived(update)
    }

    companion object : KLogging() {
    }
}