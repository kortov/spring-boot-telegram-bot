package com.kortov.bootigram.bots

import com.kortov.bootigram.bots.botlogic.Bot
import com.kortov.bootigram.bots.handlers.ResponseHandler
import com.kortov.bootigram.config.TelegramProperties
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import kotlin.test.assertEquals


@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class BotTest {

    @RelaxedMockK
    internal lateinit var responseHandler: ResponseHandler

    @Autowired
    internal lateinit var bot: Bot
    @Autowired
    internal lateinit var properties: TelegramProperties

    internal val chatId = 1337L

    @BeforeAll
    internal fun setUp() {
        bot.responseHandler = responseHandler
    }


    @Test
    internal fun canSayHelloWorld() {
        val update = Update()
        val endUser = User()
        val context = MessageContext.newContext(update, endUser, chatId)
        bot.sayHello().action().accept(context)
        verify { responseHandler.sendAsync("Hello", chatId) }
    }

    @Test
    internal fun creatorId() {
        val actual = bot.creatorId()
        val expected = properties.creatorId
        assertEquals(expected, actual)
    }
}