package com.mj.elearning24.quiz.model

data class QuizResponse(
    val lisOfQuestion: ArrayList<Quiz>,
    val id: String, val duration: String,
    val msg: String,
    val isQuizTime: Int,
    val result: QuizResult
)