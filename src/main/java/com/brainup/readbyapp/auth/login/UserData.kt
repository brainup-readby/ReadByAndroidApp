package com.brainup.readbyapp.auth.login

data class UserData(
    val CITY: String,
    val DEVICE_ID: String,
    val EMAIL_ID: String,
    val FIRST_NAME: String,
    val IS_ACTIVE: String,
    val LAST_NAME: String,
    val MIDDLE_NAME: String,
    val MOBILE_NO: Long,
    val PINCODE: String,
    val ROLE_ID: Int,
    val SESSION_TOKEN: String,
    val STATE: String,
    val USERNAME: String,
    val USER_ID: Int,
    val USER_SUBSCRIPTION: List<UserSubscription>
)