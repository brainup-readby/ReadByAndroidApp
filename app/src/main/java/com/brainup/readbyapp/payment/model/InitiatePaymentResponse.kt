package com.brainup.readbyapp.payment.model

data class InitiatePaymentResponse(
    val `data`: Data,
    val status: String,
    val statusCode: String
)