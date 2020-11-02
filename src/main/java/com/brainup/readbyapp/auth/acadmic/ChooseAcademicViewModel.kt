package com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.registration.BoardData
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.brainup.readbyapp.rest.CommonResponse

class ChooseAcademicViewModel : ViewModel() {
    private val _formState = MutableLiveData<AcademicFormState>()
    val academicFormState: LiveData<AcademicFormState> = _formState
    fun getAllCourse(): LiveData<ApiResponse<CommonResponse<List<BoardData>>>>? {
        return ApiClient.client?.getBoardDetails()
    }

    fun checkValidation(
        boardName: String,
        courseName: String,
        streamName: String,
        yearName: String,
        instituteName: String
    ) {
        when {
            boardName.isBlank() -> {
                _formState.value = AcademicFormState(R.string.select_board_error)
            }
            courseName.isBlank() -> {
                _formState.value = AcademicFormState(R.string.select_course_error)
            }
            streamName.isBlank() -> {
                _formState.value = AcademicFormState(R.string.select_stream_error)
            }
            yearName.isBlank() -> {
                _formState.value = AcademicFormState(R.string.select_year_error)
            }
            instituteName.isBlank() -> {
                _formState.value = AcademicFormState(R.string.select_institute_error)
            }
            else -> {
                _formState.value = AcademicFormState(isDataValid = true)
            }
        }

    }

    fun subscribePlan(subsData: SubscriptionModel): LiveData<ApiResponse<CommonResponse<SubscriptionModel>>>? {
        return ApiClient.client?.subscribeCourse(subsData)
    }
}