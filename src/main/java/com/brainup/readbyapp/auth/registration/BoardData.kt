package com.brainup.readbyapp.auth.registration

data class BoardData(
    val BOARD_CODE: String,
    val BOARD_ID: Long,
    val BOARD_NAME: String,
    val IS_ACTIVE: String,
    val MAS_COURSE: List<Course>
)