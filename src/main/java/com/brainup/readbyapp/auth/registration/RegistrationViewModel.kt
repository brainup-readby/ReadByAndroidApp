package com.brainup.readbyapp.com.brainup.readbyapp.auth.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.brainup.readbyapp.rest.CommonResponse
import com.brainup.readbyapp.utils.Utils

class RegistrationViewModel : ViewModel() {
    private val _formState = MutableLiveData<RegistrationFormState>()
    val registrationFormState: LiveData<RegistrationFormState> = _formState

    fun registrationFormState(firstName: String, lastName: String, mobNo: String, email: String) {
        when {
            firstName.isBlank() -> {
                _formState.value =
                    RegistrationFormState(
                        firstNameError = R.string.please_enter_name
                    )
            }
            lastName.isBlank() -> {
                _formState.value =
                    RegistrationFormState(
                        lastNameError = R.string.please_enter_last_name
                    )
            }
            mobNo.isBlank() -> {
                _formState.value =
                    RegistrationFormState(
                        mobNoError = R.string.please_enter_your_mobile_number
                    )
            }
            mobNo.length < 10 -> {
                _formState.value =
                    RegistrationFormState(
                        mobNoError = R.string.please_enter_valid_mobile_number
                    )
            }
            email.isBlank() -> {
                _formState.value =
                    RegistrationFormState(
                        emailError = R.string.please_enter_email
                    )
            }
            !Utils.isValidEmail(email) -> {
                _formState.value =
                    RegistrationFormState(
                        emailError = R.string.please_enter__valid_email
                    )
            }
            else -> {
                _formState.value =
                    RegistrationFormState(
                        isDataValid = true
                    )
            }
        }
    }

    fun registration(body: UserInfo): LiveData<ApiResponse<CommonResponse<UserData>>>? {
        return ApiClient.client?.postRegistration(body)
    }
}