package com.ecloud.apps.watermeterreader.feature.onboarding.network

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecloud.apps.watermeterreader.core.designsystem.theme.WmrTheme
import com.ecloud.apps.watermeterreader.core.ui.ProtocolDropdown
import com.ecloud.apps.watermeterreader.core.ui.UrlState
import com.ecloud.apps.watermeterreader.core.ui.UrlStateSaver
import com.ecloud.apps.watermeterreader.core.ui.UrlTextFieldUserInput
import com.ecloud.apps.watermeterreader.feature.onboarding.OnboardNavigator
import com.ecloud.apps.watermeterreader.feature.onboarding.R

@Composable
fun NetworkRoute(
    navigator: OnboardNavigator
) {
    val viewModel = hiltViewModel<NetworkScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NetworkScreen(
        state = state,
        event = viewModel::onEvent,
        onNextClick = navigator::navigateToNextScreen
    )
}

@Composable
internal fun NetworkScreen(
    state: NetworkScreenUiState,
    event: (NetworkScreenEvent) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    if (state.shouldNavigate) {
        LaunchedEffect(Unit) {
            onNextClick()
        }
    }

    Surface {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(R.string.network_settings),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )


            Spacer(modifier = Modifier.height(24.dp))

            val urlFocusRequester = remember { FocusRequester() }
            var selectedIndex by remember { mutableStateOf(0) }
            val items = listOf("http", "https")
            ProtocolDropdown(
                onItemClick = {
                    selectedIndex = it
                    urlFocusRequester.requestFocus()
                },
                items = items,
                selectedIndex = selectedIndex,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            val urlTextFieldState by rememberSaveable(stateSaver = UrlStateSaver) {
                mutableStateOf(UrlState())
            }
            val isValid = urlTextFieldState.isValid
            val url by remember(
                selectedIndex,
                urlTextFieldState.text
            ) { mutableStateOf(items[selectedIndex] + "://" + urlTextFieldState.text) }

            UrlTextFieldUserInput(
                urlTextFieldState = urlTextFieldState,
                modifier = Modifier
                    .focusRequester(urlFocusRequester)
                    .fillMaxWidth(),
                imeAction = ImeAction.Done,
                onImeAction = {
                    if (isValid) {
                        event(NetworkScreenEvent.OnTest(url))
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(
                    onClick = { event(NetworkScreenEvent.OnTest(url))},
                    enabled = isValid && !state.isLoading
                ) {
                    Text(text = stringResource(R.string.test_button))
                }

                Text(text = state.testMessage)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { event(NetworkScreenEvent.OnApply(url)) },
                modifier = if (state.isLoading) Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentWidth() else Modifier.fillMaxWidth(),
                enabled = isValid && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = stringResource(id = R.string.submit_form))
                }
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
private fun NetworkScreenPreview() {
    WmrTheme {
        NetworkScreen(
            state = NetworkScreenUiState(),
            event = {},
            onNextClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NetworkScreenLoadingPreview() {
    WmrTheme {
        NetworkScreen(
            state = NetworkScreenUiState(isLoading = true),
            event = {},
            onNextClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NetworkScreenDarkPreview() {
    WmrTheme(darkTheme = true) {
        NetworkScreen(
            state = NetworkScreenUiState(),
            event = {},
            onNextClick = {}
        )
    }
}
