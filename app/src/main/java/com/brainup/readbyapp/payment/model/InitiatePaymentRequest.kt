package com.brainup.readbyapp.payment.model

import java.io.Serializable

data class InitiatePaymentRequest(
    val COURSE_STREAM_ID: Int,
    val PAYMENT_STATUS: String,
    val TRANSACTION_AMOUNT: Int,
    val USER_ID: Int
):Serializable