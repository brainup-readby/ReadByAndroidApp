package com.brainup.readbyapp.auth.login

import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.StudentStudyState

data class UserSubscription(
    val BOARD_ID: Int,
    val COURSE_ID: Int,
    val COURSE_STREAM_ID: Int,
    val INSTITUTION_NAME: String,
    val IS_ACTIVE: String,
    val IS_EXPIRED: String,
    val MAS_BOARD: UserSelectedBoard,
    val MAS_COURSE: UserSelectedCourse,
    val MAS_COURSE_YEAR: UserSelectedYear,
    val MAS_STREAM: UserSelectedStram,
    val STUDENT_STUDY_STATE: StudentStudyState,
    val MOBILE_NO: Long,
    val STREAM_ID: Int,
    val SUBSCRIPTION_ID: Int,
    val USER_ID: Int,
    val YEAR_ID: Int
)