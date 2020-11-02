package com.brainup.readbyapp.quiz.model.model

import java.io.Serializable

data class SubmitQusResponse(
    val `data`: DataX,
    val status: String,
    val statusCode: String
):Serializable