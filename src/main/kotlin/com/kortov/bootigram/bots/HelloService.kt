package com.kortov.bootigram.bots

import org.springframework.stereotype.Service
import org.telegram.abilitybots.api.db.DBContext
import org.telegram.abilitybots.api.objects.Flag
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.objects.ReplyFlow
import org.telegram.abilitybots.api.sender.SilentSender
import org.telegram.abilitybots.api.util.AbilityUtils
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.function.Consumer
import java.util.function.Predicate

@Service
class HelloService {
    companion object {
        fun fileFlow(silent: SilentSender, db: DBContext): ReplyFlow {
            val sentFile = Reply.of(Consumer { upd: Update -> silent.send("Sir, I have a file \"" + upd.message.document.fileName +"\"", AbilityUtils.getChatId(upd)) },
                    Flag.DOCUMENT)

            val getFile = ReplyFlow.builder(db)
                    .onlyIf(hasFile())
                    .next(sentFile)
                    .build()

            return ReplyFlow.builder(db)
                    .onlyIf(hasCommand(HelloBot.UPLOAD_QUIZ))
                    .action { upd -> silent.send("Sir, pls send me a file", AbilityUtils.getChatId(upd)) }
                    .next(getFile)
                    .next(sentFile)
                    .build()
        }

        private fun hasCommand(msg: String): Predicate<Update> {
            return Flag.MESSAGE.and(Flag.TEXT).and({ upd: Update -> upd.message.text.equals("/" + msg) })
        }

        private fun hasFile(): Predicate<Update> {
            return Flag.DOCUMENT
        }
    }

}