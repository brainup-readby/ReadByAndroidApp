package com.brainup.readbyapp.auth.registration

data class Course(
    val COURSE_CODE: String,
    val COURSE_ID: Long,
    val COURSE_NAME: String,
    val IS_ACTIVE: String,
    val MAS_COURSE_YEAR: List<Year>,
    val MAS_STREAM: List<Stream>,
    val icon_path: String
)