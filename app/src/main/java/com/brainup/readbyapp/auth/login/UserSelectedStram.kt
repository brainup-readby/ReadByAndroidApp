package com.brainup.readbyapp.auth.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class UserSelectedStram(
    val MAS_SUBJECT: List<UserSelectedSubject>,
    val STREAM_CODE: String,
    val STREAM_ID: Int,
    val STREAM_NAME: String
) : Serializable