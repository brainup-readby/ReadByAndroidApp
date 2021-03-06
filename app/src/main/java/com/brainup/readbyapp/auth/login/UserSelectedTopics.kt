package com.brainup.readbyapp.auth.login

import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.MasTopicStatus
import java.io.Serializable

data class UserSelectedTopics(
    val BOOK_URL: String,
    val IS_ACTIVE: String,
    val TOPIC_CODE: String,
    val TOPIC_ID: Int,
    val TOPIC_NAME: String,
    val TOPIC_SUBSCRIPTION: String,
    val VIDEO_STATUS: String,
    val TEST_STATUS: String,
    val VIDEO_URL: String,
    val icon_path: String,
    var isEnable: Boolean,
    var MAS_TOPIC_STATUS: MasTopicStatus
) : Serializable