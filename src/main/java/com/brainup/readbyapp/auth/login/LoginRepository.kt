package com.brainup.readbyapp.com.brainup.readbyapp.auth.login

import androidx.lifecycle.LiveData
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.brainup.readbyapp.rest.CommonResponse

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository {

    // in-memory cache of the loggedInUser object
    fun postLogin(mobNo: String): LiveData<ApiResponse<CommonResponse<String>>>? {
        return ApiClient.client?.postLogin(mobNo)
    }
}