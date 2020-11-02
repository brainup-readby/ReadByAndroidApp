package com.brainup.readbyapp.quiz.model.model

data class QusRequestModelItem(
    val ANSWERED_BY: Int,
    val GIVEN_ANSWER: Int,
    val QUESTION_ID: Int,
    val TOPIC_ID: Int,
    val USER_ID: Int
)