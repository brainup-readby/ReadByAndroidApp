package com.brainup.readbyapp.quiz.model.model

import java.io.Serializable

data class DataX(
    val MAXIMUM_MARKS: Int,
    val REPORT_ID: Int,
    val TOPIC_ID: Int,
    val TOTAL_MARKS_OBTAINED: Int,
    val TOTAL_PERCENTAGE: Int,
    val USER_ID: Int,
    val OVERALL_RESULT:String

):Serializable