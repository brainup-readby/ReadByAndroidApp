package com.brainup.readbyapp.com.brainup.readbyapp.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brainup.readbyapp.R
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.brainup.readbyapp.rest.CommonResponse

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()

    fun login(mobNo: String): LiveData<ApiResponse<CommonResponse<String>>>? {
        val mob = StringBuilder()
        mob.append("+91")
        mob.append(mobNo)
        return ApiClient.client?.postLogin(mobNo)
    }

    fun checkMultipleLogin(mobNo: String): LiveData<ApiResponse<CommonResponse<Boolean>>>? {
        val mob = StringBuilder()
       // mob.append("+91")
        mob.append(mobNo)
        return ApiClient.client?.checkUserMultipleLogin(mobNo)
    }

    fun sendRegOtp(mobNo: String): LiveData<ApiResponse<CommonResponse<String>>>? {
        val mob = StringBuilder()
        mob.append("+91")
        mob.append(mobNo)
        return ApiClient.client?.getOpt(mobNo)
    }

    fun loginDataChanged(mobNo: String, isLogin: Boolean) {
        when {
            mobNo.isBlank() -> {
                _loginForm.value =
                    LoginFormState(
                        mobNoError = R.string.please_enter_your_mobile_number
                    )
            }
            mobNo.length < 10 -> {
                _loginForm.value =
                    LoginFormState(
                        mobNoError = R.string.please_enter_valid_mobile_number
                    )
            }

            else -> {
                _loginForm.value =
                    LoginFormState(
                        isDataValid = true,
                        isLoginData = isLogin
                    )
            }
        }
    }

    fun logout(mobNo: String): LiveData<ApiResponse<LogoutResponse>>? {
        val mob = StringBuilder()
        mob.append(mobNo)
        return ApiClient.client?.logout(mobNo)
    }

    /*  // A placeholder username validation check
      private fun isUserNameValid(username: String): Boolean {
          return if (username.contains('@')) {
              Patterns.EMAIL_ADDRESS.matcher(username).matches()
          } else {
              username.isNotBlank()
          }
      }

      // A placeholder password validation check
      private fun isPasswordValid(password: String): Boolean {
          return password.length > 5
      }*/
}