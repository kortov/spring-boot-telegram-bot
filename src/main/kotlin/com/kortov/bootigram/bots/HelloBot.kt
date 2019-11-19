package com.kortov.bootigram.bots

import com.kortov.bootigram.config.TelegramProperties
import org.telegram.abilitybots.api.bot.AbilityWebhookBot
import org.telegram.abilitybots.api.db.DBContext
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality.USER
import org.telegram.abilitybots.api.objects.Privacy.ADMIN
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import javax.annotation.PostConstruct

open class HelloBot(
        botToken: String,
        botUsername: String,
        botPath: String,
        dbForBot: DBContext,
        options: DefaultBotOptions,
        private val properties: TelegramProperties)
    : AbilityWebhookBot(botToken, botUsername, botPath, dbForBot, options) {

    lateinit var responseHandler: ResponseHandler

    override fun creatorId(): Int {
        return properties.creatorId
    }

    @PostConstruct
    fun init() {
        responseHandler = ResponseHandler(sender)
    }

    override fun onWebhookUpdateReceived(update: Update): BotApiMethod<*>? {
        super.onUpdateReceived(update)
        if (update.hasMessage() && update.message.hasDocument()) {
            responseHandler.getFileFromUser(update)
            val sendMessage = SendMessage()
            sendMessage.chatId = update.message.chatId!!.toString()
            sendMessage.text = "processing file"
            return sendMessage
        }
        return null
    }

    fun sayHello(): Ability {
        return Ability
                .builder()
                .name("hello")
                .info("says hello world!")
                .input(0)
                .locality(USER)
                .privacy(ADMIN)
                .action { ctx -> responseHandler.replyToStartAsync(ctx.chatId()) }
//                .post { ctx -> silent.send("Bye world!", ctx.chatId()!!) }
                .build()
    }

    fun getQuizFile(): Ability {
        return Ability
                .builder()
                .name("loadquiz")
                .info("receives a quiz json file (up to 20 MB)")
                .input(0)
                .locality(USER)
                .privacy(ADMIN)
                .action { ctx -> responseHandler.getFileFromUser(ctx.update()) }
//                .post { ctx -> silent.send("Bye world!", ctx.chatId()!!) }
                .build()
    }

    fun close() {
        db.close()
    }

}