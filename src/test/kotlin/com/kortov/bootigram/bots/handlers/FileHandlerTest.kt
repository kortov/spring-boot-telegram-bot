package com.kortov.bootigram.bots.handlers

import com.kortov.bootigram.quiz.*
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*


@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileHandlerTest {

    @RelaxedMockK
    lateinit var parser: JsonParser

    @InjectMockKs
    lateinit var fileHandler: FileHandler

    @Test
    fun testGetQuiz() {
        every { parser.parse(any<String>()) } returns Arrays.asList(Exam(
                "name", Arrays.asList(
                Chapter(0, "Chapter 1", Arrays.asList(
                        ExamQuestion(0, "questionText", "explanation",
                                Arrays.asList(Answer(1, "textAnswer")),
                                Arrays.asList(1)))))))
        val fileName = "static/quiz_simplest_sample.json"
        fileHandler.getQuiz(fileName)
        verify {
            parser.parse(any<String>())
        }
    }

}
