package com.kortov.bootigram.quiz

data class Exam (val name: String, val chapters:List<Chapter>)
data class Chapter(val id:Int, val name:String, val examQuestions: List<ExamQuestion>)
data class ExamQuestion(val id: Int, var questionText: String, var explanation: String, val answers: List<Answer>, val correctAnswersIds: List<Int>)
data class Answer(val id:Int, var textAnswer: String)