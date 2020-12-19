package com.brainup.readbyapp.com.brainup.readbyapp.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.LogoutResponse
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.brainup.readbyapp.rest.CommonResponse

class DashBoardViewModel : ViewModel() {
    var userData: MutableLiveData<UserData>? = null
    fun getUserDetails(id: String): LiveData<ApiResponse<CommonResponse<UserData>>>? {
        return ApiClient.client?.getUserInfo(id)
    }

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