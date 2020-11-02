package com.brainup.readbyapp.com.brainup.readbyapp.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.brainup.readbyapp.rest.CommonResponse

class HomeViewModel : ViewModel() {

    fun getUserDetails(id: String): LiveData<ApiResponse<CommonResponse<UserData>>>? {
        return ApiClient.client?.getUserInfo(id)
    }
}