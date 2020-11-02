package com.brainup.readbyapp.com.brainup.readbyapp.auth.otp

import androidx.lifecycle.LiveData
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.brainup.readbyapp.rest.CommonResponse

class VerifyOtpViewModel : BaseOtpViewModel() {
    fun verify(mobNo: String, otp: String): LiveData<ApiResponse<CommonResponse<Boolean>>>? {
        val mob = StringBuilder()
        mob.append("+91")
        mob.append(mobNo)
        return ApiClient.client?.otpVerification(mobNo, otp)
    }

    fun loginOtpVerify(
        mobNo: String,
        otp: String
    ): LiveData<ApiResponse<CommonResponse<UserData>>>? {
        val mob = StringBuilder()
        mob.append("+91")
        mob.append(mobNo)
        return ApiClient.client?.otpLoginVerification(mobNo, otp)
    }

    fun resendOtp(mobNo: String): LiveData<ApiResponse<CommonResponse<String>>>? {
        val mob = StringBuilder()
        mob.append("+91")
        mob.append(mobNo)
        return ApiClient.client?.resendOtp(mobNo)
    }

    /* fun createPassword(
         mobNo: String,
         otp: String,
         pwd: String
     ): LiveData<ApiResponse<Boolean>>? {
         return ApiClient.client?.createPwd(mobNo, otp, pwd)
     }*/
}