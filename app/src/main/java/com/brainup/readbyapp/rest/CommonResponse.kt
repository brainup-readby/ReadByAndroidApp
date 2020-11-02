package com.brainup.readbyapp.rest

import com.google.gson.annotations.SerializedName

data class CommonResponse<T>(
    @SerializedName("statusCode") val msg: String,
    @SerializedName("status") val status: String,
    val data: T
)