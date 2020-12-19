package com.brainup.readbyapp.auth.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class UserSelectedBoard(
    val BOARD_CODE: String,
    val BOARD_ID: Int,
    val BOARD_NAME: String
) : Serializable