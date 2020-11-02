package com.brainup.readbyapp.payment.model

data class Data(
    val CHECKSUM_VAL: String,
    val COURSE_STREAM_ID: Int,
    val ORDER_ID: String,
    val PAYMENT_STATUS: String,
    val PAY_GWAY_TRANS_ID: Any,
    val TRANSACTION_AMOUNT: Int,
    val USER_ID: Int,
    val USER_TRANS_ID: Int
)