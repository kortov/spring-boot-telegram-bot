package com.kortov.bootigram.bots.botlogic

import com.kortov.bootigram.bots.handlers.ResponseHandler
import org.telegram.abilitybots.api.db.DBContext
import org.telegram.abilitybots.api.objects.Flag
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.objects.ReplyFlow
import org.telegram.abilitybots.api.sender.SilentSender
import org.telegram.abilitybots.api.util.AbilityUtils
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

class QuizApi {
    companion object {
        fun sendQuizFlow(bot: Bot, silent: SilentSender, db: DBContext): ReplyFlow {

            val sendQuiz = Reply.of(Consumer { upd: Update ->
                val sendPoll = SendPoll(AbilityUtils.getChatId(upd), "question", Arrays.asList("a", "b", "c"))
                sendPoll.type = "quiz"
                sendPoll.correctOptionId = 1
                sendPoll.explanation = "blabla"
                bot.sendApi(sendPoll)
            }, Flag.MESSAGE)


            return ReplyFlow.builder(db)
                    .onlyIf(hasCommand(Bot.UPLOAD_QUIZ))
                    .action { upd -> silent.send("Sir I have a quiz", AbilityUtils.getChatId(upd)) }
                    .next(sendQuiz)
                    .build()
        }

        fun handleQuizAnswerFlow(bot: Bot, silent: SilentSender, db: DBContext): ReplyFlow {
            return ReplyFlow.builder(db)
                    .onlyIf(hasPollAnswer())
                    .action { upd -> ResponseHandler.logger.info { upd.poll.options } }
                    .build()
        }

        private fun hasCommand(msg: String): Predicate<Update> {
            return Flag.MESSAGE.and(Flag.TEXT).and { upd: Update -> upd.message.text == "/$msg" }
        }

        private fun hasPollAnswer(): Predicate<Update> {
            return Predicate<Update>{ upd: Update -> upd.hasPoll()}
        }

        private fun hasFile(): Predicate<Update> {
            return Flag.DOCUMENT
        }
    }
}