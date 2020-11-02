package com.mj.elearning24.quiz.model

import com.mj.elearning24.quiz.model.QuestionIdOptionSelected

data class SubmitQuestionRequestModel(
    val mobileUserId: Int,
    val onlineTestId: Int,
    val questionIdOptionSelected: ArrayList<QuestionIdOptionSelected>
)