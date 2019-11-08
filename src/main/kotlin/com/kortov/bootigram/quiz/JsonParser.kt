package com.kortov.bootigram.quiz

import com.kortov.bootigram.quiz.dto.Exam
import java.io.StringReader
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import org.springframework.core.io.DefaultResourceLoader



class JsonParser {

    fun readFileUsingGetResource(): String {
        val resourceLoader = DefaultResourceLoader()
        val resource = resourceLoader.getResource("static/test_questions.json")
        return resource.file.readText(Charsets.UTF_8)
    }

    fun parse(): ArrayList<Exam> {
        val file = readFileUsingGetResource()

        val klaxon = Klaxon()
        val exams = arrayListOf<Exam>()
        JsonReader(StringReader(file)).use { reader ->
            reader.beginArray {
                while (reader.hasNext()) {
                    val product = klaxon.parse<Exam>(reader)
                    exams.add(product!!)
                }
            }
        }

        return exams
    }

}
