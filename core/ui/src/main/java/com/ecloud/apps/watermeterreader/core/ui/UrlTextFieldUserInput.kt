package com.ecloud.apps.watermeterreader.core.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlTextFieldUserInput(
    modifier: Modifier = Modifier,
    urlTextFieldState: TextFieldState = remember { UrlState() },
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = urlTextFieldState.text,
        onValueChange = {
            urlTextFieldState.text = it
            urlTextFieldState.enableShowErrors()
        },
        modifier = modifier
            .onFocusChanged { focusState ->
                urlTextFieldState.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    urlTextFieldState.enableShowErrors()
                }
            },
        label = { Text(text = stringResource(R.string.label_url)) },
        placeholder = { Text(text = "www.google.com") },
        isError = urlTextFieldState.showErrors(),
        keyboardActions = KeyboardActions { onImeAction() },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = false,
            imeAction = imeAction,
            keyboardType = KeyboardType.Uri
        )
    )

    urlTextFieldState.getError()?.let { TextFieldError(textError = it) }
}
