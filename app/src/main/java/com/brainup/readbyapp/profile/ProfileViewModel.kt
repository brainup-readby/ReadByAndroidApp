package com.brainup.readbyapp.com.brainup.readbyapp.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.LogoutResponse
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.brainup.readbyapp.rest.CommonResponse

class ProfileViewModel:ViewModel() {

    fun logout(mobNo: String): LiveData<ApiResponse<LogoutResponse>>? {
        val mob = StringBuilder()
        mob.append(mobNo)
        return ApiClient.client?.logout(mobNo)
    }
}