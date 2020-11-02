package com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brainup.readbyapp.R
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.model.model.VideoRequestModel
import com.brainup.readbyapp.quiz.model.model.*
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse

class QuizResultViewModel(application: Application) : AndroidViewModel(application) {
    var context = application
    var quizResultData: MutableLiveData<DataX>? = null
    fun setResult(result: MutableLiveData<DataX>) {
        quizResultData = result
    }



    fun updateTopicFlag(list : TestRequestModel): LiveData<ApiResponse<TopicStatusResposeModel>>? {
        return ApiClient.client?.updateTopicFlag(list)
    }


    fun updateVideoFlag(list : VideoRequestModel): LiveData<ApiResponse<TopicStatusResposeModel>>? {
        return ApiClient.client?.updateVideoFlag(list)
    }




    fun getResult(): MutableLiveData<DataX>? {
        return quizResultData
    }

    fun getMaximumMark(): String {
        return context.getString(R.string.max_mark, quizResultData?.value?.MAXIMUM_MARKS.toString())
    }

    fun getTotalMarkObtained(): String {
        return context.getString(
            R.string.total_mark_obtained,
            quizResultData?.value?.TOTAL_MARKS_OBTAINED.toString()
        )
    }

    fun getTotalPercentage(): String {
        return context.getString(
            R.string.total_percentage,
            quizResultData?.value?.TOTAL_PERCENTAGE.toString()
        )
    }

    fun getOverAllResults(): String {
        return context.getString(R.string.overall_results,
            quizResultData?.value?.OVERALL_RESULT.toString()
        )
    }

    fun getAttemptedQuestion(): String {
        return context.getString(
            R.string.attempt_questions,
            quizResultData?.value?.TOTAL_PERCENTAGE.toString()
        )
    }

    fun getTotalQuestion(): String {
        return context.getString(
            R.string.total_questions,
            quizResultData?.value?.TOTAL_MARKS_OBTAINED.toString()
        )
    }



}
