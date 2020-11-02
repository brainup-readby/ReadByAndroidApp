package com.mj.elearning24.quiz.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Quiz(
    val question: String,
    val questionDiagram: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    @SerializedName("currectOption")
    val ans: String,
    val explanation: String,
    val explanationFigure: String,
    var selectedAns: String,
    val diagram: String,
    val id: String

) : Serializable {
    fun userSelection(userAns: String) {
        if (userAns.isEmpty()) {
            selectedAns = ""
            return
        }
        when {
            userAns.equals(option1, false) -> {
                selectedAns = option1
            }
            userAns.equals(option2, false) -> {
                selectedAns = option2
            }
            userAns.equals(option3, false) -> {
                selectedAns = option3
            }
            userAns.equals(option4, false) -> {
                selectedAns = option4
            }
        }
    }
}