package com.brainup.readbyapp.com.brainup.readbyapp.auth.model

data class ModelDisplayName(
        val title: String,
        val id: Long
) {
    override fun toString(): String {
        return title
    }
}
