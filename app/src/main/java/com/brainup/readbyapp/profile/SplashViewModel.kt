package com.brainup.readbyapp.profile;

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.LogoutResponse
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.brainup.readbyapp.rest.CommonResponse

class SplashViewModel : ViewModel() {

    fun logout(mobNo: String): LiveData<ApiResponse<LogoutResponse>>? {
        val mob = StringBuilder()
        mob.append(mobNo)
        return ApiClient.client?.logout(mobNo)
    }

    fun checkMultipleLogin(mobNo: String): LiveData<ApiResponse<CommonResponse<Boolean>>>? {
        val mob = StringBuilder()
        // mob.append("+91")
        mob.append(mobNo)
        return ApiClient.client?.checkUserMultipleLogin(mobNo)
    }
}
