package com.ecloud.apps.watermeterreader.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class UrlNameState(initialValue: String = "") : TextFieldState(
    errorFor = ::urlNameValidationError,
    validator = ::urlNameValidator,
    initialText = initialValue
)

@Suppress("UNUSED_PARAMETER")
private fun urlNameValidationError(name: String): String {
    return "Url Name is required"
}

private fun urlNameValidator(name: String): Boolean {
    return name.isNotEmpty() || name.isNotBlank()
}

@Composable
fun rememberUrlNameTextFieldState(name: String): UrlNameState =
    remember(name) {
        UrlNameState(name)
    }

val UrlNameStateSaver = textFieldStateSaver(UrlNameState())
