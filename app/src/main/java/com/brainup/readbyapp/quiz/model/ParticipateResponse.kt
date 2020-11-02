package com.mj.elearning24.quiz.model

import com.google.gson.annotations.SerializedName

data class ParticipateResponse(
    @SerializedName("isParticipated")
    val isParticipate: Boolean,
    val msg: String
)