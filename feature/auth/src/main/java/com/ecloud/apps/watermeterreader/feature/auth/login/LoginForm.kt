package com.ecloud.apps.watermeterreader.feature.auth.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ecloud.apps.watermeterreader.feature.auth.R

@Composable
internal fun LoginForm(
    onLogin: (LoginEvent.OnLogin) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Column(modifier = modifier) {
        val passwordFocusRequester = remember { FocusRequester() }
        val usernameState by rememberSaveable(stateSaver = UsernameStateSaver) {
            mutableStateOf(UsernameState())
        }
        UsernameUserInput(
            state = usernameState,
            onImeAction = {
                passwordFocusRequester.requestFocus()
            },
        )
        val size = Modifier.size(16.dp)
        Spacer(modifier = size)

        val passwordState by rememberSaveable(stateSaver = PasswordStateSaver) {
            mutableStateOf(PasswordState())
        }
        PasswordTextField(
            state = passwordState,
            modifier = Modifier.focusRequester(passwordFocusRequester),
            onImeAction = {
                onLogin(LoginEvent.OnLogin(usernameState.text, passwordState.text))
            }
        )
        Spacer(modifier = size)
        Button(
            onClick = {
                onLogin(LoginEvent.OnLogin(usernameState.text, passwordState.text))
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled && (usernameState.isValid && passwordState.isValid) && !loading,

            ) {
            if (loading) {
                CircularProgressIndicator()
            } else {
                Text(text = stringResource(R.string.sign_in))
            }
        }
    }

}