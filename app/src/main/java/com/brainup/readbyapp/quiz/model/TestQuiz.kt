package com.mj.elearning24.quiz.model

import com.google.gson.annotations.SerializedName

data class TestQuiz(
    val date_of_relies: String,
    val time_of_relies: String,
    val id: Int,
    val no_of_question: String,
    val status: Int,
    @SerializedName("quizTopic")
    val subject: String,
    @SerializedName("quizImage")
    val subjectImage: String,
    val price: String,
    var participated: Int,
    val marks: String,
    val quizDuration:String

)