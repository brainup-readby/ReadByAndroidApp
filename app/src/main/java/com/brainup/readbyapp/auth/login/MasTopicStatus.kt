package com.brainup.readbyapp.com.brainup.readbyapp.auth.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class MasTopicStatus(
    val TOPIC_STATUS_ID: Int,
    val TOPIC_ID: Int,
    val USER_ID: Int,
    val TOPIC_SUBSCRIPTION: String,
    val VIDEO_STATUS: String,
    val TEST_STATUS: String
) : Serializable