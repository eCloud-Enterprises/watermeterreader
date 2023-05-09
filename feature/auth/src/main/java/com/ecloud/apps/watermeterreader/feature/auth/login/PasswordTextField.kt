package com.ecloud.apps.watermeterreader.feature.auth.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.ecloud.apps.watermeterreader.core.designsystem.icon.EbtIcons
import com.ecloud.apps.watermeterreader.core.ui.TextFieldError
import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.feature.auth.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState = remember { PasswordState() },
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    val showPassword = rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = state.text,
        onValueChange = { state.text = it },
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = stringResource(R.string.label_password)) },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Lock, contentDescription = null)
        },
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(
                        painter = painterResource(id = EbtIcons.VisibilityOff),
                        contentDescription = null
                    )
                }
            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(
                        painter = painterResource(id = EbtIcons.Visibility),
                        contentDescription = null
                    )
                }
            }
        },
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            autoCorrect = false,
            keyboardType = KeyboardType.Password,
        ),
        keyboardActions = KeyboardActions { onImeAction() },
        isError = state.showErrors()
    )

    state.getError()?.let { TextFieldError(textError = it) }
}
