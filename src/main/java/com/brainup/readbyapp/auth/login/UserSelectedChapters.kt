package com.brainup.readbyapp.auth.login

import java.io.Serializable

data class UserSelectedChapters(
    val CHAPTER_CODE: String,
    val CHAPTER_ID: Int,
    val CHAPTER_NAME: String,
    val IS_ACTIVE: String,
    val MAS_TOPICS: List<UserSelectedTopics>,
    val icon_path: String
) : Serializable {
    fun totalTopics(): Int {
        return MAS_TOPICS.size
    }
}