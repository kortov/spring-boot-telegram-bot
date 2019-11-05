package com.kortov.bootigram.bots

import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


class ResponseHandler(private val sender: MessageSender) {

    fun replyToStart(chatId: Long) {
        try {
            sender.execute<Message, SendMessage>(SendMessage()
                    .setText("Hello world!")
                    .setChatId(chatId))
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}