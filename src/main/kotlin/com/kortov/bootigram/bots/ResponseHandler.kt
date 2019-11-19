package com.kortov.bootigram.bots

import com.beust.klaxon.Klaxon
import com.kortov.bootigram.quiz.JsonParser
import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.File
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.updateshandlers.SentCallback


class ResponseHandler(private val sender: MessageSender) {

    private val parser = JsonParser(Klaxon())

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

    fun getFileFromUser(update: Update?) {
        try {
            val fileId = update?.message?.document?.fileId
            sender.executeAsync(GetFile().setFileId(fileId),
                    object : SentCallback<File> {
                        override fun onResult(method: BotApiMethod<File>?, response: File?) {
                            val downloadFile = sender.downloadFile(response)
                            val parseFile = parser.parseFile(downloadFile)
                            print(parseFile)
                        }
                        override fun onException(method: BotApiMethod<File>?, exception: java.lang.Exception?) {}
                        override fun onError(method: BotApiMethod<File>?, apiException: TelegramApiRequestException?) {}
                    })
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}