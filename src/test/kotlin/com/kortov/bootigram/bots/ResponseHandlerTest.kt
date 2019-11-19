package com.kortov.bootigram.bots

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.updateshandlers.SentCallback

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResponseHandlerTest {

    @RelaxedMockK
    lateinit var sender: MessageSender

    @InjectMockKs
    lateinit var responseHandler: ResponseHandler
    val CHAT_ID = 1337L

    @Test
    fun canReply() {
        responseHandler.replyToStartAsync(CHAT_ID)
        verify {
            sender.executeAsync<Message, SendMessage, SentCallback<Message>>(SendMessage()
                    .setText("Hello world!")
                    .setChatId(CHAT_ID), any())
        }
    }

    @Test
    fun canHandleTelegramApiException() {
        val exception = mockk<TelegramApiException>(relaxed = true)
        every { sender.executeAsync<Message, SendMessage, SentCallback<Message>>(any(), any()) } throws exception
        responseHandler.replyToStartAsync(CHAT_ID)
        verify { exception.printStackTrace() }
    }
}