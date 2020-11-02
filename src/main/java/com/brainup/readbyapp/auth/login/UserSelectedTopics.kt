package com.brainup.readbyapp.auth.login

import java.io.Serializable

data class UserSelectedTopics(
    val BOOK_URL: String,
    val IS_ACTIVE: String,
    val TOPIC_CODE: String,
    val TOPIC_ID: Int,
    val TOPIC_NAME: String,
    val VIDEO_URL: String,
    val icon_path: String
) : Serializable