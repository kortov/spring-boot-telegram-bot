package com.kortov.bootigram.bots

import com.beust.klaxon.Klaxon
import com.kortov.bootigram.bots.handlers.FileHandler
import com.kortov.bootigram.config.TelegramProperties
import com.kortov.bootigram.quiz.JsonParser
import mu.KLogging
import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.WebhookBot
import java.io.BufferedWriter
import java.io.FileWriter
import javax.annotation.PostConstruct


@RestController("/")
class HelloController(val helloBot: WebhookBot, val fileHandler: FileHandler, val jsonParser: JsonParser) {

    @PostConstruct
    fun setup() {
//        val file = fileHandler.readFileFromResourcesAsString("static/test_questions.json")
//        val unescapeHtml4 = StringEscapeUtils.unescapeHtml4(file)
//        val withNewLines = unescapeHtml4.replace("<br>", "/n")
//        val unescaped = StringEscapeUtils.unescapeHtml4(withNewLines)
//        val writer = BufferedWriter(FileWriter("new_questions.json"))
//        writer.write(unescaped)
//        writer.close()

        val parse = jsonParser.parse(fileHandler.readFileFromResourcesAsString("static/test_questions.json")!!)
        parse.forEach { it.chapters.forEach{
            it.examQuestions.forEach {
                eq -> run {
                val unescapeHtml4 = StringEscapeUtils.unescapeHtml4(eq.questionText)
                val withNewLines = unescapeHtml4.replace("<br>", "\n")
                val unescaped = StringEscapeUtils.unescapeHtml4(withNewLines)
//                val some = Jsoup.parse(eq.questionText).text()
//                val some1 = Parser.unescapeEntities(eq.questionText, true)
//                val some2 = Parser.unescapeEntities(some, true)
//                val some3 = Jsoup.parse(some1).text()
//                val some4 = Jsoup.parse(some2).text()
//                val someSome = Parser.unescapeEntities(eq.questionText, false)
//                logger.info { unescapeHtml4!! }
//                logger.info { some }
//                logger.info { someSome }
                eq.questionText = unescaped

                val unescapeHtml41 = StringEscapeUtils.unescapeHtml4(eq.explanation)
                val withNewLines1 = unescapeHtml41.replace("<br>", "\n")
                val unescaped1 = StringEscapeUtils.unescapeHtml4(withNewLines1)
                eq.explanation = unescaped1

                eq.answers.forEach{
                    an -> run {
                    val unescapeHtml42 = StringEscapeUtils.unescapeHtml4(an.textAnswer)
                    val withNewLines2 = unescapeHtml42.replace("<br>", "\n")
                    val unescaped2 = StringEscapeUtils.unescapeHtml4(withNewLines2)
                    an.textAnswer = unescaped2
                }
                }
            }
            }
        }}

        val toJsonString = Klaxon().toJsonString(parse)

        val writer = BufferedWriter(FileWriter("new_questions.json"))
        writer.write(toJsonString)
        writer.close()
    }

    @PostMapping(TelegramProperties.WEB_HOOK)
    fun index(@RequestBody update: Update) {
        helloBot.onWebhookUpdateReceived(update)
    }

    @GetMapping("/foo")
    fun foo(): String {
        return "foo"
    }

    companion object : KLogging()
}