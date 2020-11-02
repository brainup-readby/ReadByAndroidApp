package com.brainup.readbyapp.com.brainup.readbyapp.auth.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val mobNoError: Int? = null,
    val isDataValid: Boolean = false,
    val isLoginData: Boolean = false
)