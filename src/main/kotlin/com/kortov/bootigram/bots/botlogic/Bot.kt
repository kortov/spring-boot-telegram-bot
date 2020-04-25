package com.kortov.bootigram.bots.botlogic

import com.kortov.bootigram.bots.handlers.ResponseHandler
import com.kortov.bootigram.config.TelegramProperties
import org.springframework.beans.factory.annotation.Autowired
import org.telegram.abilitybots.api.bot.AbilityWebhookBot
import org.telegram.abilitybots.api.db.DBContext
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality.USER
import org.telegram.abilitybots.api.objects.Privacy.ADMIN
import org.telegram.abilitybots.api.objects.ReplyFlow
import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.methods.BotApiMethod


open class Bot(
        private val properties: TelegramProperties,
        dbForBot: DBContext,
        options: DefaultBotOptions
) : AbilityWebhookBot(properties.botToken, properties.botUsername, TelegramProperties.WEB_HOOK, dbForBot, options) {

    @Autowired
    lateinit var responseHandler: ResponseHandler

    override fun creatorId(): Int {
        return properties.creatorId
    }

    fun sender() : MessageSender {
        return this.sender;
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

    fun fileFlow(): ReplyFlow {
        return UploadFile.fileFlow(silent, db)
    }

    fun sendQuizFlow(): ReplyFlow {
        return QuizApi.sendQuizFlow(this, silent, db)
    }

    fun handleQuizAnswerFlow(): ReplyFlow {
        return QuizApi.handleQuizAnswerFlow(this, silent, db)
    }

    fun <T : java.io.Serializable> sendApi(method: BotApiMethod<T>) {
        sendApiMethod(method)
    }

//    fun uploadFile(): Ability {
//        return Ability
//                .builder()
//                .name(UPLOAD_FILE)
//                .info("Receives a file")
//                .input(0)
//                .locality(USER)
//                .privacy(ADMIN)
//                .action { }
//                .build()
//    }
//
//    fun uploadQuiz(): Ability {
//        return Ability
//                .builder()
//                .name(UPLOAD_FILE)
//                .info("Receives a quiz")
//                .input(0)
//                .locality(USER)
//                .privacy(ADMIN)
//                .action { }
//                .build()
//    }

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

    companion object {
        const val UPLOAD_FILE = "uploadfile"
        const val UPLOAD_QUIZ = "uploadquiz"
    }


}