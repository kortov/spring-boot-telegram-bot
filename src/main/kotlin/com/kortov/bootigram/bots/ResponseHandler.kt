package com.kortov.bootigram.bots

import com.kortov.bootigram.quiz.JsonParser
import com.kortov.bootigram.quiz.dto.Exam
import mu.KLogging
import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.updateshandlers.SentCallback


class ResponseHandler(private val sender: MessageSender,
                      parser: JsonParser) {
    val parsedFile: ArrayList<Exam> = parser.parseFile("static/test_questions.json")
    val questionText: String
    init {
        questionText = parsedFile[0].chapters[0].examQuestions[1].questionText
    }

    fun replyToStartAsync(chatId: Long) {
        try {
//            val parsedFile = parser.parseFile("static/test_questions.json")
//            val questionText = parsedFile[0].chapters[0].examQuestions[1].questionText
            logger.info { questionText }
            sender.executeAsync(SendMessage()
                    .enableHtml(true)
                    .setText(questionText)
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