package com.ecloud.apps.watermeterreader.feature.auth.addediturl

import androidx.compose.foundation.layout.fillMaxWidth
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
import com.ecloud.apps.watermeterreader.core.ui.TextFieldError
import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.core.ui.UrlNameState
import com.ecloud.apps.watermeterreader.feature.auth.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Name(
    modifier: Modifier = Modifier,
    nameState: TextFieldState = remember { UrlNameState() },
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = nameState.text, onValueChange = { nameState.text = it },
        label = { Text(text = stringResource(R.string.label_name)) },
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                nameState.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    nameState.enableShowErrors()
                }
            },
        isError = nameState.showErrors(),
        keyboardActions = KeyboardActions(
            onNext = {
                onImeAction()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            autoCorrect = false
        )
    )

    nameState.getError()?.let { error -> TextFieldError(error) }
}
