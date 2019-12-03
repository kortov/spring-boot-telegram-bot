package com.kortov.bootigram.bots.handlers

import mu.KLogging
import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.updateshandlers.SentCallback

@Component
class ResponseHandler(private val sender: MessageSender) {

    fun sendAsync(message:String, chatId: Long) {
        try {
//            val parsedFile = parser.parseFile("static/test_questions.json")
//            val questionText = parsedFile[0].chapters[0].examQuestions[1].questionText
            logger.info { message }
            sender.executeAsync(SendMessage()
                    .enableHtml(true)
                    .setText(message)
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

    companion object : KLogging()
}