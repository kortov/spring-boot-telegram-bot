package com.kortov.bootigram.bots

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ResponseHandlerTest {

    @RelaxedMockK
    lateinit var sender: MessageSender

    @InjectMockKs
    lateinit var responseHandler:ResponseHandler
    val CHAT_ID = 1337L

    @Test
    fun canReply() {
        responseHandler.replyToStart(CHAT_ID)
        verify { sender.execute<Message, SendMessage>(SendMessage()
                .setText("Hello world!")
                .setChatId(CHAT_ID)) }
    }
}