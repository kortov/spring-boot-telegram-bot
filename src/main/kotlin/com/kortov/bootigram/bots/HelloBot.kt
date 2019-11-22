package com.kortov.bootigram.bots

import com.kortov.bootigram.bots.handlers.ResponseHandler
import com.kortov.bootigram.config.TelegramProperties
import org.telegram.abilitybots.api.bot.AbilityWebhookBot
import org.telegram.abilitybots.api.db.DBContext
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality.USER
import org.telegram.abilitybots.api.objects.Privacy.ADMIN
import org.telegram.telegrambots.bots.DefaultBotOptions
import javax.annotation.PostConstruct

open class HelloBot(
        private val properties: TelegramProperties,
        dbForBot: DBContext,
        options: DefaultBotOptions
) : AbilityWebhookBot(properties.botToken, properties.botUsername, TelegramProperties.WEB_HOOK, dbForBot, options) {

    lateinit var responseHandler: ResponseHandler

    override fun creatorId(): Int {
        return properties.creatorId
    }

    @PostConstruct
    fun init() {
        responseHandler = ResponseHandler(sender)
    }

//    override fun onWebhookUpdateReceived(update: Update): BotApiMethod<*>? {
//        if (update.hasMessage() && update.message.hasText()) {
//            val sendMessage = SendMessage()
//            sendMessage.chatId = update.message.chatId!!.toString()
//            sendMessage.text = "Well, all information looks like noise until you break the code."
//            return sendMessage
//        }
//        return null
//    }

    fun sayHello(): Ability {
        return Ability
                .builder()
                .name("hello")
                .info("says hello world!")
                .input(0)
                .locality(USER)
                .privacy(ADMIN)
                .action { ctx -> responseHandler.sendAsync("Hello", ctx.chatId()) }
//                .post { ctx -> silent.send("Bye world!", ctx.chatId()!!) }
                .build()
    }

    fun close() {
        db.close()
    }

}