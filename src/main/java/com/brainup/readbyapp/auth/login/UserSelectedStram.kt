package com.brainup.readbyapp.auth.login

data class UserSelectedStram(
    val MAS_SUBJECT: List<UserSelectedSubject>,
    val STREAM_CODE: String,
    val STREAM_ID: Int,
    val STREAM_NAME: String
)