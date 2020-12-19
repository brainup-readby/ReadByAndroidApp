package com.brainup.readbyapp.com.brainup.readbyapp.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RateAppResponse
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RateAppRequest
import com.brainup.readbyapp.payment.model.Data
import com.brainup.readbyapp.payment.model.InitiatePaymentRequest
import com.brainup.readbyapp.payment.model.InitiatePaymentResponse
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.brainup.readbyapp.rest.CommonResponse

class LibTopicViewModel : ViewModel() {

    fun getUserDetails(id: String): LiveData<ApiResponse<CommonResponse<UserData>>>? {
        return ApiClient.client?.getUserInfo(id)
    }

    fun getInitiatePayment(list: InitiatePaymentRequest): LiveData<ApiResponse<InitiatePaymentResponse>>? {
        return ApiClient.client?.getInitiatePayment(list)
    }

    fun sendTxnStatusDetails(list:Data): LiveData<ApiResponse<InitiatePaymentResponse>>?{
        return ApiClient.client?.sendPaymentStatus(list)
    }

}