package com.ecloud.apps.watermeterreader.feature.auth.login

import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.core.ui.textFieldStateSaver

class PasswordState(initialText: String = "") :
    TextFieldState(
        validator = ::isPasswordValid,
        errorFor = ::passwordValidationError,
        initialText = initialText
    )

@Suppress("UNUSED_PARAMETER")
private fun passwordValidationError(password: String): String {
    return "Password is required"
}

private fun isPasswordValid(password: String): Boolean {
    return password.isNotBlank() || password.isNotEmpty()
}

val PasswordStateSaver = textFieldStateSaver(PasswordState())
