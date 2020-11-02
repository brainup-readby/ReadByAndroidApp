package com.brainup.readbyapp.com.brainup.readbyapp.auth.registration

data class RegistrationFormState(
    val firstNameError: Int? = null,
    val lastNameError: Int? = null,
    val mobNoError: Int? = null,
    val emailError: Int? = null,
    val isDataValid: Boolean = false
)