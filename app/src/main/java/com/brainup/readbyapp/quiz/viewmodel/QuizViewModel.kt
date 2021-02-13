package com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RandomQuizData
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.SaveQuizResponseData
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.SaveQuizResultRequest
import com.brainup.readbyapp.payment.model.InitiatePaymentRequest
import com.brainup.readbyapp.payment.model.InitiatePaymentResponse
import com.brainup.readbyapp.quiz.model.model.QusListResponse
import com.brainup.readbyapp.quiz.model.model.QusRequestModel
import com.brainup.readbyapp.quiz.model.model.SubmitQusResponse
import com.brainup.readbyapp.rest.ApiClient
import com.brainup.readbyapp.rest.ApiResponse
import com.mj.elearning24.quiz.model.TestQuiz

class QuizViewModel : ViewModel() {
    fun getQuestionList(id: String): LiveData<ApiResponse<QusListResponse>>? {
        return ApiClient.client?.getQuizList(id)
    }

    fun submitQuestionList(list : QusRequestModel): LiveData<ApiResponse<SubmitQusResponse>>? {
        return ApiClient.client?.submitQuizList(list)
    }

    fun getRandomQuestionsData(id: String): LiveData<ApiResponse<RandomQuizData>>? {
        return ApiClient.client?.getRandomQuestionsData(id)
    }

    fun submitRandomQuizResultData(dataSaveQuizResult: SaveQuizResultRequest): LiveData<ApiResponse<SaveQuizResponseData>>?{
        return ApiClient.client?.submitRandomQuizResultData(dataSaveQuizResult)
    }
  /*  fun subscribedQuestions(
        id: String,
        questionPaperId: String
    ): LiveData<ApiResponse<ParticipateResponse>>? {
        return ApiClient.client?.participated(id, questionPaperId)
    }

    fun getQuizList(userId: String, id: String): LiveData<ApiResponse<QuizResponse>>? {
        return ApiClient.client?.getQuestionList(userId, id)
    }

    fun submitQuiz(body: SubmitQuestionRequestModel): LiveData<ApiResponse<SubmitQuizResponse>>? {
        return ApiClient.client?.submitQuiz(body)
    }*/
}
