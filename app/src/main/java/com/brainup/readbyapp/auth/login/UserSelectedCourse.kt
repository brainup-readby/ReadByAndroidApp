package com.brainup.readbyapp.auth.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class UserSelectedCourse(
    val COURSE_CODE: String,
    val COURSE_ID: Int,
    val COURSE_NAME: String,
    val COURSE_PRICE:Double
) : Serializable