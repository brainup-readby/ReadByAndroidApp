package com.brainup.readbyapp.com.brainup.readbyapp.auth.login

import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.DataRateResponse

data class LogoutResponse(
    val `data`: LogoutDataModel,
    val status: String,
    val statusCode: String
)