package com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model

import java.io.Serializable


data class SaveQuizResponseData(

        val `data`: SaveQuizResponse,
        val status: String,
        val statusCode: String
): Serializable