package com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model

import java.io.Serializable

data class RandomQuizResponse(
        val QUIZ_ID: Int = 0,
        val SUBJECT_ID: Int = 0,
        val QUSTN_CODE: String = "",
        val QUSTN_DSC: String = "",
        val OPTN_1: String = "",
        val OPTN_2: String = "",
        val OPTN_3: String = "",
        val OPTN_4: String = "",
        val CRRCT_OPTN: String = "",
        var SET_SELECTION_1: Boolean,
        var SET_SELECTION_2: Boolean,
        var SET_SELECTION_3: Boolean,
        var SET_SELECTION_4: Boolean,
        var NOT_ATTEMPTED: Boolean
): Serializable
