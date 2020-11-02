package com.brainup.readbyapp.com.brainup.readbyapp.auth.registration

data class UserInfo(
    val USER_ID: String = "",
    val USERNAME: String = "",
    val ROLE_ID: Int = 1,
    val FIRST_NAME: String = "",
    val MIDDLE_NAME: String = "",
    val LAST_NAME: String = "",
    val MOBILE_NO: String = "",
    val EMAIL_ID: String = "",
    val CITY: String = "",
    val STATE: String = "",
    val PINCODE: String = "",
    val IS_ACTIVE: String = "",
    val DEVICE_ID: String = "android",
    val SESSION_TOKEN: String = ""
)