package com.mj.elearning24.quiz.model

import java.io.Serializable

data class QuizResult(
    val attemptQuestion: Int,
    val correctQuestion: Int,
    val marks: Double,
    val totalQuestion: Int,
    val unattemptQuestion: Int,
    val wrongQuestion: Int
):Serializable