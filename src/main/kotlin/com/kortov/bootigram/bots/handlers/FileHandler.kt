package com.kortov.bootigram.bots.handlers

import com.beust.klaxon.Klaxon
import com.kortov.bootigram.quiz.Exam
import com.kortov.bootigram.quiz.JsonParser
import org.apache.commons.text.StringEscapeUtils
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.BufferedWriter
import java.io.FileWriter

@Component
class FileHandler(val jsonParser: JsonParser) {
    fun getQuiz(fileName: String): List<Exam> {
        val string = readFileFromResourcesAsString(fileName)
        return jsonParser.parse(string!!)
    }

    private val resourceLoader: DefaultResourceLoader by lazy {
        val resourceLoader = DefaultResourceLoader()
        resourceLoader
    }

    fun readFileFromResourcesAsString(fileName: String): String? {
        val resource: Resource? = resourceLoader.getResource(fileName)
        return resource?.file?.readText(Charsets.UTF_8)
    }

    fun cleanUpQuiz() {
        val file = readFileFromResourcesAsString("static/test_questions.json")
//        val unescapeHtml4 = StringEscapeUtils.unescapeHtml4(file)
//        val withNewLines = unescapeHtml4.replace("<br>", "/n")
//        val unescaped = StringEscapeUtils.unescapeHtml4(withNewLines)
//        val writer = BufferedWriter(FileWriter("new_questions.json"))
//        writer.write(unescaped)
//        writer.close()

        val parse = jsonParser.parse(readFileFromResourcesAsString("static/test_questions.json")!!)
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
}