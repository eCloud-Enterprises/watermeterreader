package com.ecloud.apps.watermeterreader.feature.auth.login

import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.core.ui.textFieldStateSaver

class UsernameState(initialText: String = "") :
    TextFieldState(
        validator = ::isUsernameValid,
        errorFor = ::usernameValidationError,
        initialText = initialText
    )

@Suppress("UNUSED_PARAMETER")
private fun usernameValidationError(username: String): String {
    return "Username is required"
}

private fun isUsernameValid(username: String): Boolean {
    return username.isNotEmpty() || username.isNotBlank()
}

val UsernameStateSaver = textFieldStateSaver(UsernameState())
