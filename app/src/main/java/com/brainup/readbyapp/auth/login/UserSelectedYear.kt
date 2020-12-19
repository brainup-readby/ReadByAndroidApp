package com.brainup.readbyapp.auth.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class UserSelectedYear(
    val YEAR: Int,
    val YEAR_ID: Int,
    val  DISPLAY_NAME:String

    ) : Serializable