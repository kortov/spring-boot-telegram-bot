package com.kortov.bootigram.bots

import com.kortov.bootigram.config.TelegramProperties
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.WebhookBot


@RestController("/")
class HelloController(val helloBot: WebhookBot) {

    @PostMapping(TelegramProperties.WEB_HOOK)
    fun index(@RequestBody update: Update) {
        helloBot.onWebhookUpdateReceived(update)
    }

    @GetMapping("/foo")
    fun foo(): String {
        return "foo"
    }

    companion object : KLogging()
}