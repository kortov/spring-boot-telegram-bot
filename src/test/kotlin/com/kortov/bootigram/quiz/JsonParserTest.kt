package com.kortov.bootigram.quiz

import com.beust.klaxon.Klaxon
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonParserTest {

    private lateinit var jsonParser: JsonParser

    @BeforeAll
    internal fun init() {
        jsonParser = JsonParser(Klaxon())
    }

    @Test
    internal fun testParseString() {
        val fileName = "static/quiz_simplest_sample.json"
        val file = DefaultResourceLoader().getResource(fileName).file
        val result = jsonParser.parse(file)
        val expected = listOf(Exam("name",
                listOf(Chapter(0, "Chapter 1",
                        listOf(ExamQuestion(0, "questionText", "explanation",
                                listOf(Answer(1, "textAnswer")),
                                listOf(1)))))))
        Assertions.assertEquals(
                expected, result)
    }

    @Test
    internal fun testParseEmptyString() {
        val string = ""
        val result = jsonParser.parse(string)
        val expected = emptyList<Exam>()
        Assertions.assertEquals(
                expected, result)
    }
}
