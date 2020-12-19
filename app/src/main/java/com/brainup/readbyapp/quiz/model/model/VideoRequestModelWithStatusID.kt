package com.brainup.readbyapp.com.brainup.readbyapp.quiz.model.model

data class VideoRequestModelWithStatusID(

    val TOPIC_ID: Int,
    val TOPIC_STATUS_ID: Int,
    val VIDEO_STATUS: String,
    val TEST_STATUS: String,
    val TOPIC_SUBSCRIPTION: String,
    val USER_ID: Int
)