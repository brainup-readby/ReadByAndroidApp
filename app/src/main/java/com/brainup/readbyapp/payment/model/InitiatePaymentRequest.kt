package com.brainup.readbyapp.payment.model

import java.io.Serializable

data class InitiatePaymentRequest(

        val SUBJECT_ID:Int,
        val PAYMENT_STATUS: String,
        val TRANSACTION_AMOUNT: Double,
        val USER_ID: Int,
        val COURSE_ID: Int
):Serializable