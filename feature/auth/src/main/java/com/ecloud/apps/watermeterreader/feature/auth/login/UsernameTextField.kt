package com.ecloud.apps.watermeterreader.feature.auth.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import com.ecloud.apps.watermeterreader.core.designsystem.icon.EbtIcons
import com.ecloud.apps.watermeterreader.core.ui.TextFieldError
import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.feature.auth.login.UsernameState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameUserInput(
    modifier: Modifier = Modifier,
    state: TextFieldState = remember { UsernameState() },
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
) {
    OutlinedTextField(
        value = state.text,
        onValueChange = { state.text = it },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                state.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    state.enableShowErrors()
                }
            },
        label = { Text(text = "Username") },
        leadingIcon = {
            Icon(
                imageVector = EbtIcons.Person,
                contentDescription = null
            )
        },
        keyboardActions = KeyboardActions { onImeAction() },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            autoCorrect = false
        ),
        isError = state.showErrors()
    )

    state.getError()?.let { TextFieldError(textError = it) }
}
