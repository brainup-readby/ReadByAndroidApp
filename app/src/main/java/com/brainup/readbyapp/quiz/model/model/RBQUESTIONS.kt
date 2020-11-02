package com.brainup.readbyapp.quiz.model.model

data class RBQUESTIONS(
    val ISANSWERED: String,
    val ISMANDATORY: String,
    val ISRANDOMIZED: String,
    val IS_ACTIVE: String,
    val QIMAGEPATH: String,
    val QUESTIONDESC: String,
    val QUESTIONID: Int,
    val QUESTIONTITLE: String,
    val RB_MULTIPLE_OPTION: List<RBMULTIPLEOPTION>,
    val RB_QUESTION_TYPE: RBQUESTIONTYPE
)