package com.kortov.bootigram.bots

import com.kortov.bootigram.config.TelegramBotConfig
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User


@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = [TelegramBotConfig::class])
@SpringBootTest
class HelloBotTest {

    @RelaxedMockK
    lateinit var responseHandler: ResponseHandler


    @Autowired
    lateinit var bot: HelloBot

    val CHAT_ID = 1337L

    @BeforeAll
    fun setUp() {
        bot.responseHandler = responseHandler
    }


    @Test
    fun canSayHelloWorld() {
        val update = Update()
        val endUser = User()
        val context = MessageContext.newContext(update, endUser, CHAT_ID)
        bot.sayHello().action().accept(context)
        verify { responseHandler.replyToStart(CHAT_ID) }
    }

}