package com.kortov.bootigram.bots

import com.kortov.bootigram.bots.handlers.ResponseHandler
import com.kortov.bootigram.config.TelegramProperties
import org.telegram.abilitybots.api.bot.AbilityWebhookBot
import org.telegram.abilitybots.api.db.DBContext
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Flag.*
import org.telegram.abilitybots.api.objects.Locality.USER
import org.telegram.abilitybots.api.objects.Privacy.ADMIN
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.objects.ReplyFlow
import org.telegram.abilitybots.api.util.AbilityUtils.getChatId
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.function.Consumer
import java.util.function.Predicate
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

    fun directionFlow(): ReplyFlow {
        val saidLeft = Reply.of(Consumer { upd: Update -> silent.send("Sir, I have gone left.", getChatId(upd)) },
                hasMessageWith("go left or else"))

        val leftflow = ReplyFlow.builder(db)
                .action { upd -> silent.send("I don't know how to go left.", getChatId(upd)) }
                .onlyIf(hasMessageWith("left"))
                .next(saidLeft).build()

        val saidRight = Reply.of(Consumer { upd: Update -> silent.send("Sir, I have gone right.", getChatId(upd)) },
                hasMessageWith("right"))

        return ReplyFlow.builder(db)
                .action { upd -> silent.send("Command me to go left or right!", getChatId(upd)) }
                .onlyIf(hasMessageWith("wake up"))
                .next(leftflow)
                .next(saidRight)
                .build()
    }

    private fun hasMessageWith(msg: String): Predicate<Update> {
        return Predicate { upd: Update -> upd.message.text.equals(msg, ignoreCase = true) }
    }

    fun fileFlow(): ReplyFlow {
        val sentFile = Reply.of(Consumer { upd: Update -> silent.send("Sir, I have a file \"" + upd.message.document.fileName +"\"", getChatId(upd)) },
                DOCUMENT)

        val getFile = ReplyFlow.builder(db)
                .onlyIf(hasFile())
                .next(sentFile)
                .build()

        return ReplyFlow.builder(db)
                .onlyIf(hasCommand(UPLOAD_QUIZ))
                .action { upd -> silent.send("Sir, pls send me a file", getChatId(upd)) }
                .next(getFile)
                .next(sentFile)
                .build()
    }

    private fun hasCommand(msg: String): Predicate<Update> {
        return MESSAGE.and(TEXT).and({ upd: Update -> upd.message.text.equals("/" + msg) })
    }

    private fun hasFile(): Predicate<Update> {
        return DOCUMENT
    }

    fun uploadQuiz(): Ability {
        return Ability
                .builder()
                .name(UPLOAD_QUIZ)
                .info("Receives a quiz file")
                .input(0)
                .locality(USER)
                .privacy(ADMIN)
                .action { }
                .build()
    }

    fun sayHello(): Ability {
        return Ability
                .builder()
                .name("hello")
                .info("Says hello world!")
                .input(0)
                .locality(USER)
                .privacy(ADMIN)
                .action { ctx -> responseHandler.sendAsync("Hello", ctx.chatId()) }
                .build()
    }

    fun close() {
        db.close()
    }

    companion object {
        const val UPLOAD_QUIZ = "uploadquiz"
    }

}