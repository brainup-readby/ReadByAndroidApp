package com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model

import java.io.Serializable

data class SaveQuizResponse(
        val QUIZ_RESULT_ID: Int = 0,
        val USER_ID: Int = 0,
        val MOBILE_NO: Long = 0,
        val TOTAL_NO_QSTN: Int = 0,
        val TOTAL_NO_QSTN_ATMPT: Int = 0,
        val TOTAL_NO_CORRCT_ANS: Int = 0
): Serializable
