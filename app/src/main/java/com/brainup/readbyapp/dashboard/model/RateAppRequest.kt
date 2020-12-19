package com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model


data class RateAppRequest(
    val USER_ID: Int = 0,
    val MOBILE_NO: Long = 0,
    val LEARNING: Int = 0,
    val USABILITY: Int = 0,
    val CONTENT: String = "",
    val COMMENTS: String = ""
)