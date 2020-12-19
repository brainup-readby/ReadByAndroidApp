package com.brainup.readbyapp.payment.model

data class Data(

        val USER_TRANS_ID: Int,
        val USER_ID: Int,
        val ORDER_ID: String,
        val COURSE_ID: Int,
        val SUBJECT_ID:Int,
        val PAYMENT_STATUS: String,
        val TRANSACTION_AMOUNT: Int,
        val CHECKSUM_VAL: String,
        val BANKNAME:String,
        val MID: String,
        val TXNID: String,
        val RESPCODE: String,
        val PAYMENTMODE: String,
        val BANKTXNID: String,
        val GATEWAYNAME: String,
        val TXNDATE: String,
        val CURRENCY: String,
        val SUBSCRIPTION_ID: Int
)