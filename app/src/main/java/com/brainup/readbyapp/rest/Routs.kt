package com.brainup.readbyapp.rest

object Routs {
    const val SEND_OTP = "readBy/sendOTP"
    const val RESEND_OTP = "readBy/resendOTP"
    const val OTP_VERIFICATION = "readBy/verifyOTP"
    const val REGISTRATION = "readBy/registerStudentDetail"
    const val OTP_LOGIN_VERIFICATION = "readBy/verifyLoginOTP"
    const val BOARD_DETAILS = "readBy/getBoardDetail"
    const val LOGIN = "readBy/loginStudent"
    const val SUBSCRIBE_COURSE = "readBy/registerStudentSubscription"
    const val USER_INFO = "readBy/getUserDetails"
    const val GET_QUIZ_LIST = "readBy/getQuestionByTopic"
    const val SUBMIT_QUIZ_LIST = "readBy/saveStudentAnswer"
    const val UPDATE_TOPIC_STATUS = "readBy/updateTopicFlag"
    const val GET_INITIAL_PAYMENTS = "readBy/saveUserTransaction"
    const val LOGOUT_USER = "getLogoutDetail"
    const val RATE_APP_API = "readBy/saveFeedBack"
    const val CHECK_Multiple_LOGIN = "getLoginDetail"
    const val SEND_PAYMENT_STATUS = "readBy/updateUserTransaction"
    const val GET_RANDOM_QUIZ = "readBy/getRandomQuiz"
    const val SAVE_RANDOM_QUIZ_RESULT = "readBy/saveRandonQuizResult "
}