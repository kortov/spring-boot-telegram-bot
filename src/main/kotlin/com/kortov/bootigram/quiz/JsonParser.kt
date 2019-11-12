package com.kortov.bootigram.quiz

import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.kortov.bootigram.quiz.dto.Exam
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.stereotype.Component
import java.io.StringReader


@Component
class JsonParser(val klaxon: Klaxon) {

    private fun readFileUsingGetResource(fileName: String): String {
        val resourceLoader = DefaultResourceLoader()
        val resource = resourceLoader.getResource(fileName)
        return resource.file.readText(Charsets.UTF_8)
    }

    fun parse(fileName: String): ArrayList<Exam> {
        val file = readFileUsingGetResource(fileName)
        val exams = arrayListOf<Exam>()
        JsonReader(StringReader(file)).use { reader ->
            reader.beginArray {
                while (reader.hasNext()) {
                    val product = klaxon.parse<Exam>(reader)
                    product?.let { exams.add(product) }
                }
            }
        }
        return exams
    }

}
