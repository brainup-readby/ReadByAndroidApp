package com.brainup.readbyapp.quiz.model.model

data class Data(
    val DETAIL_DESC: String,
    val IMAGEPATH: String,
    val IS_ACTIVE: Any,
    val QCOUNT: Int,
    val QID: Int,
    val QNAME: String,
    val RB_QUESTIONS: List<RBQUESTIONS>,
    val TOPIC_ID: Int
)