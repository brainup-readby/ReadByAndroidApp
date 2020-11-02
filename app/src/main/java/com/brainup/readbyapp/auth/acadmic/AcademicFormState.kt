package com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic

data class AcademicFormState(
    val boardNameError: Int? = null,
    val courseNameError: Int? = null,
    val streamNameError: Int? = null,
    val yearNameError: Int? = null,
    val instituteNameError: Int? = null,
    val isDataValid: Boolean = false
)