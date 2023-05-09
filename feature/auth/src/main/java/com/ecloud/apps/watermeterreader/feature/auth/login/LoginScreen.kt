package com.ecloud.apps.watermeterreader.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.feature.auth.AuthNavigator
import com.ecloud.apps.watermeterreader.feature.auth.R
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun LoginScreen(navigator: AuthNavigator, snackbarHostState: SnackbarHostState) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LoginScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        navigateToNetworkSettings = navigator::navigateToNetworkSettings,
        onEvent = viewModel::onEvent
    )
}

@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginUiState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEvent: (LoginEvent) -> Unit = {},
    navigateToNetworkSettings: () -> Unit = {},
) {
    state.userMessage?.let {
        LaunchedEffect(snackbarHostState, state, it) {
            snackbarHostState.showSnackbar(it)
            onEvent(LoginEvent.OnSnackbarShown)
        }
    }

    Surface {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.size(24.dp))

            val focusManager = LocalFocusManager.current

            LoginForm(onLogin = { event ->
                onEvent(event)
                focusManager.clearFocus()
            })

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = navigateToNetworkSettings) {
                Text(text = stringResource(R.string.network_settings))
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    WmrTheme {
        LoginScreen(state = LoginUiState())
    }
}

@Preview
@Composable
fun LoginScreenPreviewDark() {
    WmrTheme(darkTheme = true) {
        LoginScreen(state = LoginUiState())
    }
}