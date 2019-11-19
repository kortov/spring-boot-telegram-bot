package com.kortov.bootigram.bots

import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.updateshandlers.SentCallback


class ResponseHandler(private val sender: MessageSender) {

    fun replyToStartAsync(chatId: Long) {
        try {
            sender.executeAsync(SendMessage()
                    .setText("Hello world!")
                    .setChatId(chatId),
                    object : SentCallback<Message> {
                        override fun onResult(method: BotApiMethod<Message>, sentMessage: Message?) {}
                        override fun onError(botApiMethod: BotApiMethod<Message>, e: TelegramApiRequestException) {}
                        override fun onException(botApiMethod: BotApiMethod<Message>, e: Exception) {}
                    })
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}