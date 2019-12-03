package com.kortov.bootigram.quiz

import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import mu.KLogging
import org.springframework.stereotype.Component
import java.io.File
import java.io.StringReader


@Component
class JsonParser(val klaxon: Klaxon) {

    fun parse(file: File): List<Exam> {
        return parse(file.readText(Charsets.UTF_8))
    }

    fun parse(string: String): List<Exam> {
        val exams = arrayListOf<Exam>()
        try {
            JsonReader(StringReader(string)).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        val product = klaxon.parse<Exam>(reader)
                        product?.let { exams.add(product) }
                    }
                }
            }
        } catch (e: Exception) {
            logger.info { e.printStackTrace() }
        }
        return exams
    }

    companion object : KLogging()
}
