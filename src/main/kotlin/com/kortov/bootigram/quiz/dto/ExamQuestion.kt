package com.kortov.bootigram.quiz.dto

data class ExamQuestion(val id: Int, val questionText: String, val explanation: String,
                   val answers: List<Answer>, val correctAnswersIds: List<Int>)