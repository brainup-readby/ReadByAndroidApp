package com.brainup.readbyapp.com.brainup.readbyapp.auth.login

import com.brainup.readbyapp.auth.login.UserSubscription

data class LogoutDataModel(
    val LOGIN_ID: Int,
    val USER_ID: Int,
    val MOBILE_NO: Long,
    val TOKEN: String,
    val LOGIN_FLAG: String
)