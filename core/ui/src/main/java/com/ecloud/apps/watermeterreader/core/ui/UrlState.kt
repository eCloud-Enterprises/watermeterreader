package com.ecloud.apps.watermeterreader.core.ui

import android.util.Patterns

class UrlState(initialValue: String = "") : TextFieldState(
    errorFor = ::urlValidationError,
    validator = ::isUrlValid,
    initialText = initialValue
)

@Suppress("UNUSED_PARAMETER")
private fun urlValidationError(url: String): String {
    return "Invalid Url"
}

private fun isUrlValid(url: String): Boolean {
    return Patterns.WEB_URL.matcher(url)
        .matches() || "localhost(:[0-9]+)?$".toRegex().matches(url)
}

val UrlStateSaver = textFieldStateSaver(UrlState())
