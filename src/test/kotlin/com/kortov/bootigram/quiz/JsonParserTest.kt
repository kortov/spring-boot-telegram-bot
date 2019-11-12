package com.kortov.bootigram.quiz

import com.beust.klaxon.Klaxon
import com.kortov.bootigram.quiz.dto.Answer
import com.kortov.bootigram.quiz.dto.Chapter
import com.kortov.bootigram.quiz.dto.Exam
import com.kortov.bootigram.quiz.dto.ExamQuestion
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*


@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonParserTest {

    lateinit var jsonParser: JsonParser

    @BeforeAll
    fun init() {
        jsonParser = spyk(JsonParser(Klaxon()))
    }

    @Test
    fun testParse() {
        val fileName = "fileName"
        val file = "[\n" +
                "  {\n" +
                "    \"name\": \"name\",\n" +
                "    \"chapters\": [\n" +
                "      {\n" +
                "        \"id\": 0,\n" +
                "        \"name\": \"Chapter 1\",\n" +
                "        \"examQuestions\": [\n" +
                "          {\n" +
                "            \"id\": 0,\n" +
                "            \"questionText\": \"questionText\",\n" +
                "            \"explanation\": \"explanation\",\n" +
                "            \"answers\": [\n" +
                "              {\n" +
                "                \"id\": 1,\n" +
                "                \"textAnswer\": \"textAnswer\"\n" +
                "              }\n" +
                "            ],\n" +
                "            \"correctAnswersIds\": [\n" +
                "              1\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]"
        every { jsonParser invoke "readFileUsingGetResource" withArguments listOf(fileName) } returns file

        val result = jsonParser.parse(fileName)
        val expected = Arrays.asList(Exam(
                "name", Arrays.asList(
                Chapter(0, "Chapter 1", Arrays.asList(
                        ExamQuestion(0, "questionText", "explanation",
                                Arrays.asList(Answer(1, "textAnswer")),
                                Arrays.asList(1)))))))
        Assertions.assertEquals(
                expected, result)
    }
}
