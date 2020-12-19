package com.brainup.readbyapp.com.brainup.readbyapp.auth.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class StudentStudyState(
    val USER_ID: String,
    val TOPIC_ID: String,
    val STATE_ID: String,
    val VIDEO_LEFT_TIME: String
) : Serializable