package com.tarbiyah.ailearn.ui.quiz

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
