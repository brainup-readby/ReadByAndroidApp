package com.brainup.readbyapp.auth.login

import java.io.Serializable

data class UserSelectedSubject(
    val IS_ACTIVE: String,
    val MAS_CHAPTERS: List<UserSelectedChapters>,
    val STREAM_ID: Int,
    val SUBJECT_CODE: String,
    val SUBJECT_ID: Int,
    val SUBJECT_NAME: String,
    val icon_path: String
) : Serializable